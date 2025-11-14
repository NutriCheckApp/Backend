package com.nutricheck.backend.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 연령대/성별별 권장 영양소 섭취량 엔티티
 * Required nutrients by age range and gender entity
 */
@Entity
@Table(name = "required_nutrients",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"age_range", "gender"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Essential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nutrient_id")
    private Long nutrientId;

    /**
     * 연령대 (예: "20-29", "30-39")
     * Age range (e.g., "20-29", "30-39")
     */
    @Column(name = "age_range", nullable = false, length = 10)
    private String ageRange;

    /**
     * 성별 (M: 남성, F: 여성)
     * Gender (M: Male, F: Female)
     */
    @Column(nullable = false, length = 1)
    private String gender;

    // ========================================
    // 권장 섭취량 / Required Intake
    // ========================================

    /**
     * 권장 칼로리 (kcal)
     * Required calories in kcal
     */
    @Column(name = "req_calories")
    private Double reqCalories;

    /**
     * 권장 탄수화물 (g)
     * Required carbohydrate in grams
     */
    @Column(name = "req_carbohydrate")
    private Double reqCarbohydrate;

    /**
     * 권장 단백질 (g)
     * Required protein in grams
     */
    @Column(name = "req_protein")
    private Double reqProtein;

    /**
     * 권장 지방 (g)
     * Required fat in grams
     */
    @Column(name = "req_fat")
    private Double reqFat;

    /**
     * 권장 포화지방 (g)
     * Required saturated fat in grams
     */
    @Column(name = "req_saturated_fat")
    private Double reqSaturatedFat;

    /**
     * 권장 트랜스지방 (g)
     * Required trans fat in grams
     */
    @Column(name = "req_trans_fat")
    private Double reqTransFat;

    /**
     * 권장 당류 (g)
     * Required sugar in grams
     */
    @Column(name = "req_sugar")
    private Double reqSugar;

    /**
     * 권장 나트륨 (mg)
     * Required sodium in milligrams
     */
    @Column(name = "req_sodium")
    private Double reqSodium;

    /**
     * 권장 콜레스테롤 (mg)
     * Required cholesterol in milligrams
     */
    @Column(name = "req_cholesterol")
    private Double reqCholesterol;

    /**
     * 권장 칼슘 (mg)
     * Required calcium in milligrams
     */
    @Column(name = "req_calcium")
    private Double reqCalcium;

    /**
     * 권장 식이섬유 (g)
     * Required fiber in grams
     */
    @Column(name = "req_fiber")
    private Double reqFiber;
}
