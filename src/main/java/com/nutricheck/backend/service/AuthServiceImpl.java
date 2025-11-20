package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.*;
import com.nutricheck.backend.repository.UserRepository;
import com.nutricheck.backend.security.jwt.JwtGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final MetricsCalculationService metricsCalculationService;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public AuthResponse register(RegisterRequest request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new RuntimeException("이미 존재하는 ID입니다.");
        }

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return AuthResponse.builder()
                .jwt(token)
                .message("회원가입에 성공했습니다.")
                .username(request.getUsername())
                .build();
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

    @Override
    public UpdateUserMetricsResponse updateUserMetrics(UpdateUserMetricsRequest request) {
        // 사용자 조회 / Find User
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자 정보 업데이트 / Update UserInfo
        user.setHeight(request.getHeight());
        user.setWeight(request.getWeight());
        user.setGoalWeight(request.getGoalWeight());
        user.setActivityLevel(request.getActivityLevel().name());

        // 목표 유형 자동 설정 / goal_type set automatically
        if (request.getGoalWeight() != null) {
            if (request.getGoalWeight() > request.getWeight()) {
                user.setGoalType(User.GoalType.GAIN);  // 목표체중 > 현재체중 → 증량
            } else if (request.getGoalWeight().equals(request.getWeight())) {
                user.setGoalType(User.GoalType.MAINTAIN);  // 목표체중 = 현재체중 → 유지
            } else {
                user.setGoalType(User.GoalType.DIET);  // 목표체중 < 현재체중 → 다이어트
            }
        }

        // BMR 계산 및 저장 / calculate BMR and save
        double bmr = metricsCalculationService.calculateBMR(
                user.getGender(),
                user.getWeight(),
                user.getHeight(),
                user.getAge()
        );
        user.setBmr(bmr);

        // TDEE 계산 및 저장 / Calculate TDEE and save
        double tdee = metricsCalculationService.calculateTDEE(bmr, request.getActivityLevel());
        user.setTdee(tdee);

        // 사용자 정보 저장 / Save into user table
        userRepository.save(user);

        return UpdateUserMetricsResponse.builder()
                .message("건강 정보가 업데이트되었습니다.")
                .bmr(Math.round(bmr * 100.0) / 100.0) // 소수점 2자리
                .tdee(Math.round(tdee * 100.0) / 100.0) // 소수점 2자리
                .build();
    }
}
