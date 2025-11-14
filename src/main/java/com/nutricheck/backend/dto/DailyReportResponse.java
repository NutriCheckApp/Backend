package com.nutricheck.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 일일 영양 분석 리포트 응답 DTO
 * Daily nutritional analysis report response DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyReportResponse {

    /**
     * 분석 ID
     * Analysis ID
     */
    private Long analysisId;

    /**
     * 사용자 ID
     * User ID
     */
    private Long userId;

    /**
     * 분석 날짜
     * Analysis date
     */
    private LocalDate analysisDate;

    // ========================================
    // 실제 섭취량 / Actual Intake
    // ========================================

    /**
     * 총 칼로리 (kcal)
     * Total calories
     */
    private Double totalCalories;

    /**
     * 총 탄수화물 (g)
     * Total carbohydrate
     */
    private Double totalCarbohydrate;

    /**
     * 총 단백질 (g)
     * Total protein
     */
    private Double totalProtein;

    /**
     * 총 지방 (g)
     * Total fat
     */
    private Double totalFat;

    /**
     * 총 나트륨 (mg)
     * Total sodium
     */
    private Double totalSodium;

    /**
     * 총 콜레스테롤 (mg)
     * Total cholesterol
     */
    private Double totalCholesterol;

    /**
     * 총 칼슘 (mg)
     * Total calcium
     */
    private Double totalCalcium;

    // ========================================
    // 권장 섭취량 / Required Intake
    // ========================================

    /**
     * 권장 칼로리 (kcal)
     * Required calories
     */
    private Double reqCalories;

    /**
     * 권장 탄수화물 (g)
     * Required carbohydrate
     */
    private Double reqCarbohydrate;

    /**
     * 권장 단백질 (g)
     * Required protein
     */
    private Double reqProtein;

    /**
     * 권장 지방 (g)
     * Required fat
     */
    private Double reqFat;

    /**
     * 권장 나트륨 (mg)
     * Required sodium
     */
    private Double reqSodium;

    /**
     * 권장 콜레스테롤 (mg)
     * Required cholesterol
     */
    private Double reqCholesterol;

    /**
     * 권장 칼슘 (mg)
     * Required calcium
     */
    private Double reqCalcium;

    // ========================================
    // 상태 / Status
    // ========================================

    /**
     * 칼로리 상태 (OVER, NORMAL, UNDER)
     * Calories status
     */
    private String caloriesStatus;

    /**
     * 탄수화물 상태
     * Carbohydrate status
     */
    private String carbohydrateStatus;

    /**
     * 단백질 상태
     * Protein status
     */
    private String proteinStatus;

    /**
     * 지방 상태
     * Fat status
     */
    private String fatStatus;

    /**
     * 나트륨 상태
     * Sodium status
     */
    private String sodiumStatus;

    /**
     * 콜레스테롤 상태
     * Cholesterol status
     */
    private String cholesterolStatus;

    /**
     * 칼슘 상태
     * Calcium status
     */
    private String calciumStatus;

    // ========================================
    // AI 분석 결과 / AI Analysis
    // ========================================

    /**
     * AI 종합 분석
     * AI summary
     */
    private String aiSummary;

    /**
     * AI 개선 제안
     * AI recommendations
     */
    private String aiRecommendations;
}
