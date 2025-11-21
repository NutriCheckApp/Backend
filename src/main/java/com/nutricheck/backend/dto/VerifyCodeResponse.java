package com.nutricheck.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 인증 코드 검증 응답 DTO
 * Verify code response DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCodeResponse {

    /**
     * 검증 결과 (true: 유효, false: 무효)
     * Validation result (true: valid, false: invalid)
     */
    private boolean valid;
}