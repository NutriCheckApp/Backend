package com.nutricheck.backend.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 추천 식단 상세 엔티티
 * Meal plan detail entity
 */
@Entity
@Table(name = "meal_plan_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealPlanDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long detailId;

    /**
     * 주간 식단표 (다대일 관계)
     * Weekly meal plan reference (Many-to-One)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private WeeklyMealPlan weeklyMealPlan;

    /**
     * 요일 (1=월요일, 7=일요일)
     * Day of week (1=Monday, 7=Sunday)
     */
    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    /**
     * 식사 시간대 (다대일 관계)
     * Meal type reference (Many-to-One)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_type_id", nullable = false)
    private MealType mealType;

    /**
     * 식품 (다대일 관계)
     * Food reference (Many-to-One)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    /**
     * 추천 섭취량 (g)
     * Recommended amount in grams
     */
    @Column(name = "recommended_amount")
    private Double recommendedAmount;

    /**
     * 예상 칼로리 (kcal)
     * Estimated calories in kcal
     */
    @Column(name = "estimated_calories")
    private Double estimatedCalories;
}