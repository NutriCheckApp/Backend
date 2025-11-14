package com.nutricheck.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 식품 정보 응답 DTO
 * Food information response DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodResponse {

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
     * 기준 제공량 (g)
     * Serving size
     */
    private Double servingSize;

    /**
     * 제공량 단위
     * Serving unit
     */
    private String servingUnit;

    // ========================================
    // 기본 영양소 (100g 기준) / Basic Nutrients (per 100g)
    // ========================================

    /**
     * 칼로리 (kcal)
     * Calories
     */
    private Double calories;

    /**
     * 탄수화물 (g)
     * Carbohydrate
     */
    private Double carbohydrate;

    /**
     * 단백질 (g)
     * Protein
     */
    private Double protein;

    /**
     * 지방 (g)
     * Fat
     */
    private Double fat;

    // ========================================
    // 세부 영양소 / Detailed Nutrients
    // ========================================

    /**
     * 포화지방 (g)
     * Saturated fat
     */
    private Double saturatedFat;

    /**
     * 트랜스지방 (g)
     * Trans fat
     */
    private Double transFat;

    /**
     * 불포화지방 (g)
     * Unsaturated fat
     */
    private Double unsaturatedFat;

    /**
     * 당류 (g)
     * Sugar
     */
    private Double sugar;

    /**
     * 나트륨 (mg)
     * Sodium
     */
    private Double sodium;

    /**
     * 콜레스테롤 (mg)
     * Cholesterol
     */
    private Double cholesterol;

    /**
     * 칼슘 (mg)
     * Calcium
     */
    private Double calcium;

    /**
     * 식이섬유 (g)
     * Fiber
     */
    private Double fiber;
}
