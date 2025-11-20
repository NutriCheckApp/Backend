package com.nutricheck.backend.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 인증 코드 전송 요청 DTO
 * Send verification code request DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendVerificationCodeRequest {

    /**
     * 이메일
     * Email address
     */
    @NotBlank(message = "Email required")
    @Email(message = "Invalid email format")
    private String email;
}