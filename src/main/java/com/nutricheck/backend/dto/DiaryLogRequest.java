package com.nutricheck.backend.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 식단 기록 요청 DTO
 * Diary log request DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiaryLogRequest {

    /**
     * 식품 ID
     * Food ID
     */
    @NotNull(message = "Food ID required")
    private Long foodId;

    /**
     * 섭취량 (g)
     * Intake amount in grams
     */
    @NotNull(message = "Intake amount required")
    @Positive(message = "Intake amount must be positive")
    private Double intakeAmount;

    /**
     * 섭취 날짜
     * Intake date
     */
    private LocalDate intakeDate;

    /**
     * 식사 시간대 (BREAKFAST, LUNCH, DINNER, SNACK)
     * Meal type
     */
    @NotNull(message = "Meal type required")
    private String mealType;
}
