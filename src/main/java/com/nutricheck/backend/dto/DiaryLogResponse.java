package com.nutricheck.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 식단 기록 응답 DTO
 * Diary log response DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiaryLogResponse {

    /**
     * 섭취 기록 ID
     * Intake ID
     */
    private Long intakeId;

    /**
     * 식품 ID
     * Food ID
     */
    private Long foodId;

    /**
     * 식품명
     * Food name
     */
    private String foodName;

    /**
     * 섭취량 (g)
     * Intake amount in grams
     */
    private Double intakeAmount;

    /**
     * 섭취 날짜
     * Intake date
     */
    private LocalDate intakeDate;

    /**
     * 섭취 시간
     * Intake time
     */
    private LocalDateTime intakeTime;

    /**
     * 식사 시간대 (BREAKFAST, LUNCH, DINNER, SNACK)
     * Meal type
     */
    private String mealType;

    /**
     * 식사 시간대 표시명 (아침, 점심, 저녁, 간식)
     * Meal type display name
     */
    private String mealTypeDisplayName;

    /**
     * 칼로리 (섭취량 기준 계산)
     * Calories based on intake amount
     */
    private Double calories;

    /**
     * 탄수화물 (g)
     * Carbohydrate in grams
     */
    private Double carbohydrate;

    /**
     * 단백질 (g)
     * Protein in grams
     */
    private Double protein;

    /**
     * 지방 (g)
     * Fat in grams
     */
    private Double fat;
}
