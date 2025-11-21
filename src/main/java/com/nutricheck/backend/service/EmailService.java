package com.nutricheck.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * 이메일 전송 서비스
 * Email sending service
 */
@Service
@Slf4j
public class EmailService {

    private final Random random = new Random();

    /**
     * 6자리 인증 코드 생성
     * Generate 6-digit verification code
     */
    public String generateVerificationCode() {
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    /**
     * 인증 코드 이메일 전송
     * Send verification code email
     *
     * @param email 수신 이메일
     * @param code 인증 코드
     */
    public void sendVerificationCode(String email, String code) {
        // TODO: 실제 이메일 전송 구현 (SMTP 설정 필요) / Implementation of actual email transfer (requires SMTP setup)
        // 현재는 개발 환경이므로 로그로 출력 / Currently, it is a development environment, so output as log
        log.info("=".repeat(50));
        log.info("이메일 인증 코드 전송");
        log.info("수신자: {}", email);
        log.info("인증 코드: {}", code);
        log.info("유효 시간: 5분");
        log.info("=".repeat(50));

        // 실제 이메일 전송 코드 예시 / Example of actual email transfer(After spring-boot-starter-mail configuration in pom.yml):
        /*
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("NutriCheck 이메일 인증");
        message.setText("인증 코드: " + code + "\n\n이 코드는 5분간 유효합니다.");
        mailSender.send(message);
        */
    }
}