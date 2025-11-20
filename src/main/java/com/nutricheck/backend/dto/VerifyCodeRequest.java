package com.nutricheck.backend.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 인증 코드 검증 요청 DTO
 * Verify code request DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCodeRequest {

    /**
     * 이메일
     * Email address
     */
    @NotBlank(message = "Email required")
    @Email(message = "Invalid email format")
    private String email;

    /**
     * 인증 코드
     * Verification code
     */
    @NotBlank(message = "Code required")
    private String code;
}