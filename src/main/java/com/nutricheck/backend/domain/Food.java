package com.nutricheck.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 식품 정보 엔티티
 * Food information entity
 */
@Entity
@Table(name = "food")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long foodId;

    /**
     * 식품명
     * Food name
     */
    @Column(name = "food_name", nullable = false, length = 100)
    private String foodName;

    /**
     * 기준 제공량 (g)
     * Serving size in grams
     */
    @Column(name = "serving_size")
    @Builder.Default
    private Double servingSize = 100.0;

    /**
     * 제공량 단위
     * Serving unit
     */
    @Column(name = "serving_unit", length = 20)
    @Builder.Default
    private String servingUnit = "g";

    // ========================================
    // 기본 영양소 / Basic Nutrients
    // ========================================

    /**
     * 칼로리 (kcal)
     * Calories in kcal
     */
    @Builder.Default
    private Double calories = 0.0;

    /**
     * 탄수화물 (g)
     * Carbohydrate in grams
     */
    @Builder.Default
    private Double carbohydrate = 0.0;

    /**
     * 단백질 (g)
     * Protein in grams
     */
    @Builder.Default
    private Double protein = 0.0;

    /**
     * 지방 (g)
     * Fat in grams
     */
    @Builder.Default
    private Double fat = 0.0;

    // ========================================
    // 세부 영양소 / Detailed Nutrients
    // ========================================

    /**
     * 포화지방 (g)
     * Saturated fat in grams
     */
    @Column(name = "saturated_fat")
    @Builder.Default
    private Double saturatedFat = 0.0;

    /**
     * 트랜스지방 (g)
     * Trans fat in grams
     */
    @Column(name = "trans_fat")
    @Builder.Default
    private Double transFat = 0.0;

    /**
     * 불포화지방 (g)
     * Unsaturated fat in grams
     */
    @Column(name = "unsaturated_fat")
    @Builder.Default
    private Double unsaturatedFat = 0.0;

    /**
     * 당류 (g)
     * Sugar in grams
     */
    @Builder.Default
    private Double sugar = 0.0;

    /**
     * 나트륨 (mg)
     * Sodium in milligrams
     */
    @Builder.Default
    private Double sodium = 0.0;

    /**
     * 콜레스테롤 (mg)
     * Cholesterol in milligrams
     */
    @Builder.Default
    private Double cholesterol = 0.0;

    /**
     * 칼슘 (mg)
     * Calcium in milligrams
     */
    @Builder.Default
    private Double calcium = 0.0;

    // ========================================
    // 추가 영양소 (향후 확장) / Additional Nutrients (Future expansion)
    // ========================================

    /**
     * 식이섬유 (g)
     * Dietary fiber in grams
     */
    @Builder.Default
    private Double fiber = 0.0;

    /**
     * 비타민 A (μg)
     * Vitamin A in micrograms
     */
    @Column(name = "vitamin_a")
    @Builder.Default
    private Double vitaminA = 0.0;

    /**
     * 비타민 C (mg)
     * Vitamin C in milligrams
     */
    @Column(name = "vitamin_c")
    @Builder.Default
    private Double vitaminC = 0.0;

    /**
     * 철분 (mg)
     * Iron in milligrams
     */
    @Builder.Default
    private Double iron = 0.0;

    /**
     * 생성일시
     * Creation timestamp
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
