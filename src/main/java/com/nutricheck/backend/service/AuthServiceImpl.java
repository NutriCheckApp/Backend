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
}
