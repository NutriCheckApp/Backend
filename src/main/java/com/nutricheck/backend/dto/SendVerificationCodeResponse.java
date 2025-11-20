package com.nutricheck.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 인증 코드 전송 응답 DTO
 * Send verification code response DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendVerificationCodeResponse {

    /**
     * 성공 메시지
     * Success message
     */
    private String message;

    /**
     * 이메일
     * Email address
     */
    private String email;
}