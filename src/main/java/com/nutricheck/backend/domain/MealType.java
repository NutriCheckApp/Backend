package com.nutricheck.backend.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 식사 시간대 엔티티 (아침, 점심, 저녁, 간식)
 * Meal type entity (Breakfast, Lunch, Dinner, Snack)
 */
@Entity
@Table(name = "meal_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_type_id")
    private Long mealTypeId;

    /**
     * 식사 시간대 코드 (BREAKFAST, LUNCH, DINNER, SNACK)
     * Meal type code
     */
    @Column(name = "meal_name", nullable = false, unique = true, length = 20)
    private String mealName;

    /**
     * 표시명 (아침, 점심, 저녁, 간식)
     * Display name in Korean
     */
    @Column(name = "display_name", nullable = false, length = 20)
    private String displayName;
}