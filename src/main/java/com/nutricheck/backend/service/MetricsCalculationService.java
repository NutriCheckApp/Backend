package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.ActivityLevel;
import org.springframework.stereotype.Service;

/**
 * BMR 및 TDEE 계산 서비스
 * BMR and TDEE calculation service
 */
@Service
public class MetricsCalculationService {

    /**
     * BMR(기초대사량) 계산
     * Calculate Basal Metabolic Rate
     *
     * @param gender 성별 (M: 남성, F: 여성)
     * @param weight 체중 (kg)
     * @param height 키 (cm)
     * @param age 나이
     * @return BMR (kcal)
     */
    public double calculateBMR(String gender, double weight, double height, int age) {
        if ("M".equalsIgnoreCase(gender)) {
            // 남자: 66.5 + (13.75 × 체중) + (5.003 × 키) - (6.75 × 나이)
            return 66.5 + (13.75 * weight) + (5.003 * height) - (6.75 * age);
        } else if ("F".equalsIgnoreCase(gender)) {
            // 여자: 655.1 + (9.563 × 체중) + (1.850 × 키) - (4.676 × 나이)
            return 655.1 + (9.563 * weight) + (1.850 * height) - (4.676 * age);
        } else {
            throw new IllegalArgumentException("Invalid gender. Must be 'M' or 'F'");
        }
    }

    /**
     * TDEE(활동대사량) 계산
     * Calculate Total Daily Energy Expenditure
     *
     * @param bmr 기초대사량
     * @param activityLevel 활동 수준
     * @return TDEE (kcal)
     */
    public double calculateTDEE(double bmr, ActivityLevel activityLevel) {
        return bmr * activityLevel.getMultiplier();
    }
}