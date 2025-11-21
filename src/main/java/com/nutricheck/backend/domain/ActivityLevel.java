package com.nutricheck.backend.domain;

import lombok.Getter;

/**
 * 활동 수준 열거형
 * Activity level enumeration
 */
@Getter
public enum ActivityLevel {
    /**
     * 좌식 (운동을 거의 또는 전혀 하지 않음)
     * Sedentary (little or no exercise)
     */
    SEDENTARY(1.2, "좌식"),

    /**
     * 가벼운 활동 (가벼운 운동/스포츠 1-3일/주)
     * Lightly active (light exercise/sports 1-3 days/week)
     */
    LIGHTLY_ACTIVE(1.375, "가벼운 활동"),

    /**
     * 적당히 활동적 (중간 정도의 운동/스포츠 3-5일/주)
     * Moderately active (moderate exercise/sports 3-5 days/week)
     */
    MODERATELY_ACTIVE(1.55, "적당히 활동적"),

    /**
     * 매우 활동적 (일주일에 6-7일 격렬한 운동/스포츠)
     * Very active (hard exercise/sports 6-7 days/week)
     */
    VERY_ACTIVE(1.725, "매우 활동적"),

    /**
     * 극도로 활동적 (매우 힘든 운동/스포츠 및 육체노동)
     * Extremely active (very hard exercise/sports & physical job)
     */
    EXTREMELY_ACTIVE(1.9, "극도로 활동적");

    private final double multiplier;
    private final String description;

    ActivityLevel(double multiplier, String description) {
        this.multiplier = multiplier;
        this.description = description;
    }
}