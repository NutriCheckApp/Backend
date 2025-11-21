package com.nutricheck.backend.dto;


import com.nutricheck.backend.domain.ActivityLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 건강 정보 업데이트 요청 DTO
 * Update user metrics request DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserMetricsRequest {

    /**
     * 사용자 이름 (로그인 ID)
     * Username for identification
     */
    @NotBlank(message = "Username required")
    private String username;

    /**
     * 키 (cm)
     * Height in centimeters
     */
    @NotNull(message = "Height required")
    @Positive(message = "Height must be positive")
    private Double height;

    /**
     * 현재 체중 (kg)
     * Current weight in kilograms
     */
    @NotNull(message = "Weight required")
    @Positive(message = "Weight must be positive")
    private Double weight;

    /**
     * 목표 체중 (kg)
     * Goal weight in kilograms
     */
    @Positive(message = "Goal weight must be positive")
    private Double goalWeight;

    /**
     * 활동 수준
     * Activity level
     */
    @NotNull(message = "Activity level required")
    private ActivityLevel activityLevel;
}