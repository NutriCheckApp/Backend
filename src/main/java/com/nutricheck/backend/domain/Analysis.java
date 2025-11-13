package com.nutricheck.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 일일 영양 분석 결과 엔티티
 * Daily nutritional analysis result entity
 */
@Entity
@Table(name = "daily_analysis",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "analysis_date"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id")
    private Long analysisId;

    /**
     * 사용자 (다대일 관계)
     * User reference (Many-to-One)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 분석 날짜
     * Analysis date
     */
    @Column(name = "analysis_date", nullable = false)
    private LocalDate analysisDate;

    // ========================================
    // 실제 섭취량 (계산된 값) / Actual Intake (Calculated)
    // ========================================

    /**
     * 총 칼로리 (kcal)
     * Total calories in kcal
     */
    @Column(name = "total_calories")
    private Double totalCalories;

    /**
     * 총 탄수화물 (g)
     * Total carbohydrate in grams
     */
    @Column(name = "total_carbohydrate")
    private Double totalCarbohydrate;

    /**
     * 총 단백질 (g)
     * Total protein in grams
     */
    @Column(name = "total_protein")
    private Double totalProtein;

    /**
     * 총 지방 (g)
     * Total fat in grams
     */
    @Column(name = "total_fat")
    private Double totalFat;

    /**
     * 총 나트륨 (mg)
     * Total sodium in milligrams
     */
    @Column(name = "total_sodium")
    private Double totalSodium;

    /**
     * 총 콜레스테롤 (mg)
     * Total cholesterol in milligrams
     */
    @Column(name = "total_cholesterol")
    private Double totalCholesterol;

    /**
     * 총 칼슘 (mg)
     * Total calcium in milligrams
     */
    @Column(name = "total_calcium")
    private Double totalCalcium;

    // ========================================
    // 권장 섭취량 대비 상태 / Status vs. Required Intake
    // ========================================

    /**
     * 칼로리 상태 (OVER: 초과, NORMAL: 적정, UNDER: 부족)
     * Calories status
     */
    @Column(name = "calories_status", length = 10)
    @Enumerated(EnumType.STRING)
    private NutrientStatus caloriesStatus;

    /**
     * 탄수화물 상태
     * Carbohydrate status
     */
    @Column(name = "carbohydrate_status", length = 10)
    @Enumerated(EnumType.STRING)
    private NutrientStatus carbohydrateStatus;

    /**
     * 단백질 상태
     * Protein status
     */
    @Column(name = "protein_status", length = 10)
    @Enumerated(EnumType.STRING)
    private NutrientStatus proteinStatus;

    /**
     * 지방 상태
     * Fat status
     */
    @Column(name = "fat_status", length = 10)
    @Enumerated(EnumType.STRING)
    private NutrientStatus fatStatus;

    /**
     * 나트륨 상태
     * Sodium status
     */
    @Column(name = "sodium_status", length = 10)
    @Enumerated(EnumType.STRING)
    private NutrientStatus sodiumStatus;

    /**
     * 콜레스테롤 상태
     * Cholesterol status
     */
    @Column(name = "cholesterol_status", length = 10)
    @Enumerated(EnumType.STRING)
    private NutrientStatus cholesterolStatus;

    /**
     * 칼슘 상태
     * Calcium status
     */
    @Column(name = "calcium_status", length = 10)
    @Enumerated(EnumType.STRING)
    private NutrientStatus calciumStatus;

    // ========================================
    // AI 분석 결과 / AI Analysis Results
    // ========================================

    /**
     * AI 종합 분석 (사용자의 식단에 대한 전반적인 평가)
     * AI comprehensive summary
     */
    @Column(name = "ai_summary", columnDefinition = "TEXT")
    private String aiSummary;

    /**
     * AI 개선 제안 (목표에 맞춘 식단 개선 방안)
     * AI recommendations for improvement
     */
    @Column(name = "ai_recommendations", columnDefinition = "TEXT")
    private String aiRecommendations;

    /**
     * 생성일시
     * Creation timestamp
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 영양소 상태 열거형
     * Nutrient status enumeration
     */
    public enum NutrientStatus {
        OVER,      // 초과 / Exceeds recommended intake
        NORMAL,    // 적정 / Within recommended range
        UNDER      // 부족 / Below recommended intake
    }
}
