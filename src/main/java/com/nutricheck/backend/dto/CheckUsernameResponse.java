package com.nutricheck.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Username 중복 체크 응답 DTO
 * Username duplicate check response DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckUsernameResponse {

    /**
     * 중복 여부 (true: 중복됨, false: 사용 가능)
     * Duplicate status (true: exists, false: available)
     */
    private boolean exists;
}