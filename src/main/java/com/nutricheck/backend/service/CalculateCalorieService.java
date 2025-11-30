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

    /**
     * 하루 권장 조단백질 계산 (g)
     * Calculate daily recommended crude protein
     *
     * 기준: 칼로리의 25%를 단백질로 (1g = 4kcal)
     * Standard: 25% of calories from protein (1g = 4kcal)
     *
     * @param dailyCalories 하루 권장 칼로리 / Daily calories
     * @return 하루 권장 조단백질 (g) / Daily recommended crude protein in grams
     */
    double calculateDailyProtein(double dailyCalories);

    /**
     * 하루 권장 조지방 계산 (g)
     * Calculate daily recommended crude fat
     *
     * 기준: 칼로리의 15%를 지방으로 (1g = 9kcal)
     * Standard: 15% of calories from fat (1g = 9kcal)
     *
     * @param dailyCalories 하루 권장 칼로리 / Daily calories
     * @return 하루 권장 조지방 (g) / Daily recommended crude fat in grams
     */
    double calculateDailyFat(double dailyCalories);

    /**
     * 하루 권장 조섬유 계산 (g)
     * Calculate daily recommended crude fiber
     *
     * 기준: 칼로리의 3%를 섬유로 (1g = 4kcal)
     * Standard: 3% of calories from fiber (1g = 4kcal)
     *
     * @param dailyCalories 하루 권장 칼로리 / Daily calories
     * @return 하루 권장 조섬유 (g) / Daily recommended crude fiber in grams
     */
    double calculateDailyFiber(double dailyCalories);

    /**
     * 하루 권장 칼슘 계산 (mg)
     * Calculate daily recommended calcium
     *
     * 기준: 체중 kg당 60mg
     * Standard: 60mg per kg of body weight
     *
     * @param weightKg 체중 (kg) / Weight in kg
     * @return 하루 권장 칼슘 (mg) / Daily recommended calcium in milligrams
     */
    double calculateDailyCalcium(double weightKg);
}

