package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.Pet;
import com.nutricheck.backend.domain.PetActivityLevel;
import com.nutricheck.backend.domain.PetLifeStage;

public interface CalculateCalorieService {


    /**
     * 반려견 하루 권장 칼로리 계산
     * Calculate daily recommended calories for pet
     *
     * 공식 / Formula:
     * - RER (기초대사량) = 70 × (체중 kg)^0.75
     * - MER (하루 필요 칼로리) = RER × 활동계수
     *
     * @param pet 반려견 엔티티 / Pet entity
     * @return 하루 권장 칼로리 (kcal/day) / Daily recommended calories
     */
    double calculateDailyCalories(Pet pet);

    /**
     * 체중, 생애 단계, 활동 수준으로 하루 권장 칼로리 계산
     * Calculate daily calories by weight, life stage, and activity level
     *
     * @param weightKg      체중 (kg) / Weight in kg
     * @param lifeStage     생애 단계 / Life stage
     * @param activityLevel 활동 수준 / Activity level
     * @return 하루 권장 칼로리 (kcal/day) / Daily recommended calories
     */
    double calculateDailyCalories(double weightKg, PetLifeStage lifeStage, PetActivityLevel activityLevel);

    /**
     * 나이와 성별 기반 생애 단계 추정
     * Estimate life stage based on age and gender
     *
     * @param pet 반려견 엔티티 / Pet entity
     * @return 추정된 생애 단계 / Estimated life stage
     */
    PetLifeStage estimateLifeStage(Pet pet);
}

