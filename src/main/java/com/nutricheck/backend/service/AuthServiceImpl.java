package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.Gender;
import com.nutricheck.backend.domain.Pet;
import com.nutricheck.backend.domain.PetActivityLevel;
import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.*;
import com.nutricheck.backend.repository.UserRepository;
import com.nutricheck.backend.security.jwt.JwtGenerator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final JwtGenerator jwtGenerator;

    private final EmailService emailService;

    private final VerificationCodeStorage verificationCodeStorage;

    private final CalculateCalorieService calculateCalorieService;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new RuntimeException("이미 존재하는 ID입니다.");
        }

        // User 엔티티 생성
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();

        // Pet 엔티티 생성
        Pet pet = Pet.builder()
                .petName(request.getPetName() == null ? "My pet" : request.getPetName())  // 기본 이름 (나중에 프로필에서 수정 가능)
                .petWeight(request.getPetWeight())
                .petAge(request.getPet_age())
                .petGender(parseGender(request.getGender()))
                .activityLevel(parseActivityLevel(request.getActivity_level()))
                .user(user)
                .build();

        // 생애 단계 추정
        pet.setLifeStage(calculateCalorieService.estimateLifeStage(pet));

        // 하루 권장 칼로리 계산
        double dailyCalories = calculateCalorieService.calculateDailyCalories(pet);
        pet.setDailyCalories(dailyCalories);

        // 영양소 계산 (AAFCO 기준, 임시, 사료에서 역추정해서 제공 예정)
        double dailyProtein = calculateCalorieService.calculateDailyProtein(dailyCalories);
        double dailyFat = calculateCalorieService.calculateDailyFat(dailyCalories);
        double dailyFiber = calculateCalorieService.calculateDailyFiber(dailyCalories);
        double dailyCalcium = calculateCalorieService.calculateDailyCalcium(request.getPetWeight());

        pet.setDailyCrudeProtein(dailyProtein);
        pet.setDailyCrudeFat(dailyFat);
        pet.setDailyCrudeFiber(dailyFiber);
        pet.setDailyCalcium(dailyCalcium);

        // User에 Pet 추가
        user.getPets().add(pet);

        // User 저장 (cascade로 Pet도 함께 저장됨)
        userRepository.save(user);

        // 인증 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);

        // RegisterResponse 반환
        return RegisterResponse.builder()
                .jwt(token)
                .message("회원가입에 성공했습니다.")
                .username(request.getUsername())
                .dailyCalories(dailyCalories)
                .dailyCrudeProtein(dailyProtein)
                .dailyCrudeFat(dailyFat)
                .dailyCrudeFibers(dailyFiber)
                .dailyCalcium(dailyCalcium)
                .build();
    }

    /**
     * Gender 문자열을 Gender enum으로 변환
     * 기본값: null (estimateLifeStage에서 미중성화로 처리됨)
     */
    private Gender parseGender(String gender) {
        return Gender.getGender(gender).orElse(null);
    }

    /**
     * ActivityLevel 문자열을 PetActivityLevel enum으로 변환
     * 기본값: NORMAL (보통)
     */
    private PetActivityLevel parseActivityLevel(String activityLevel) {
        if (activityLevel == null || activityLevel.isBlank()) {
            return PetActivityLevel.NORMAL;
        }
        try {
            return PetActivityLevel.valueOf(activityLevel.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PetActivityLevel.NORMAL;
        }
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return AuthResponse.builder()
                .jwt(token)
                .message("로그인 성공")
                .username(request.getUsername())
                .build();
    }

    @Override
    public CheckUsernameResponse checkUsername(String username) {
        boolean exists = userRepository.findByUsername(username).isPresent();
        return CheckUsernameResponse.builder()
                .exists(exists)
                .build();
    }

    @Override
    public SendVerificationCodeResponse sendVerificationCode(SendVerificationCodeRequest request) {
        String email = request.getEmail();

        // 인증 코드 생성 (6자리 숫자) / Generate Code(6 digits)
        String code = emailService.generateVerificationCode();

        // 인증 코드 저장 (5분 유효) / Save Code (Valid Time: 5 minutes)
        verificationCodeStorage.saveCode(email, code, 5);

        // 이메일 전송 / Send Code to Email
        emailService.sendVerificationCode(email, code);

        return SendVerificationCodeResponse.builder()
                .message("인증 코드가 이메일로 전송되었습니다.")
                .email(email)
                .build();
    }

    @Override
    public VerifyCodeResponse verifyCode(VerifyCodeRequest request) {
        boolean isValid = verificationCodeStorage.verifyCode(request.getEmail(), request.getCode());
        return VerifyCodeResponse.builder()
                .valid(isValid)
                .build();
    }
}
