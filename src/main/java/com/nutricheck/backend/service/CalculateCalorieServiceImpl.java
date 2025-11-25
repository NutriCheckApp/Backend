package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.Gender;
import com.nutricheck.backend.domain.Pet;
import com.nutricheck.backend.domain.PetActivityLevel;
import com.nutricheck.backend.domain.PetLifeStage;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

@Service
public class CalculateCalorieServiceImpl implements CalculateCalorieService {


    private static final Map<PetLifeStage, Map<PetActivityLevel, Double>> FACTOR_MATRIX;

    static {
        FACTOR_MATRIX = new EnumMap<>(PetLifeStage.class);

        // 퍼피 (4개월 미만) - 활동량에 관계없이 높은 계수, 과도한 활동 제한
        Map<PetActivityLevel, Double> puppyUnder4 = new EnumMap<>(PetActivityLevel.class);
        puppyUnder4.put(PetActivityLevel.INACTIVE, 2.5);
        puppyUnder4.put(PetActivityLevel.NORMAL, 3.0);
        puppyUnder4.put(PetActivityLevel.ACTIVE, 3.0);      // 어린 퍼피는 과도한 활동 권장하지 않음
        puppyUnder4.put(PetActivityLevel.VERY_ACTIVE, 3.0); // 어린 퍼피는 과도한 활동 권장하지 않음
        FACTOR_MATRIX.put(PetLifeStage.PUPPY_UNDER_4_MONTHS, puppyUnder4);

        // 퍼피 (4~12개월)
        Map<PetActivityLevel, Double> puppy4To12 = new EnumMap<>(PetActivityLevel.class);
        puppy4To12.put(PetActivityLevel.INACTIVE, 1.8);
        puppy4To12.put(PetActivityLevel.NORMAL, 2.0);
        puppy4To12.put(PetActivityLevel.ACTIVE, 2.2);
        puppy4To12.put(PetActivityLevel.VERY_ACTIVE, 2.2);  // 성장기 퍼피는 과도한 활동 제한
        FACTOR_MATRIX.put(PetLifeStage.PUPPY_4_TO_12_MONTHS, puppy4To12);

        // 성견 (중성화)
        Map<PetActivityLevel, Double> adultNeutered = new EnumMap<>(PetActivityLevel.class);
        adultNeutered.put(PetActivityLevel.INACTIVE, 1.4);
        adultNeutered.put(PetActivityLevel.NORMAL, 1.6);
        adultNeutered.put(PetActivityLevel.ACTIVE, 1.8);
        adultNeutered.put(PetActivityLevel.VERY_ACTIVE, 2.0);
        FACTOR_MATRIX.put(PetLifeStage.ADULT_NEUTERED, adultNeutered);

        // 성견 (미중성화)
        Map<PetActivityLevel, Double> adultIntact = new EnumMap<>(PetActivityLevel.class);
        adultIntact.put(PetActivityLevel.INACTIVE, 1.6);
        adultIntact.put(PetActivityLevel.NORMAL, 1.8);
        adultIntact.put(PetActivityLevel.ACTIVE, 2.0);
        adultIntact.put(PetActivityLevel.VERY_ACTIVE, 2.5);
        FACTOR_MATRIX.put(PetLifeStage.ADULT_INTACT, adultIntact);

        // 시니어
        Map<PetActivityLevel, Double> senior = new EnumMap<>(PetActivityLevel.class);
        senior.put(PetActivityLevel.INACTIVE, 1.2);
        senior.put(PetActivityLevel.NORMAL, 1.4);
        senior.put(PetActivityLevel.ACTIVE, 1.6);
        senior.put(PetActivityLevel.VERY_ACTIVE, 1.8);
        FACTOR_MATRIX.put(PetLifeStage.SENIOR, senior);

        // 특수 상태 (활동량과 무관한 고정 계수)
        // 임신 후기
        Map<PetActivityLevel, Double> pregnantLate = createFixedFactorMap(3.0);
        FACTOR_MATRIX.put(PetLifeStage.PREGNANT_LATE, pregnantLate);

        // 수유 중
        Map<PetActivityLevel, Double> lactating = createFixedFactorMap(4.0);
        FACTOR_MATRIX.put(PetLifeStage.LACTATING, lactating);

        // 체중 감량
        Map<PetActivityLevel, Double> weightLoss = createFixedFactorMap(1.0);
        FACTOR_MATRIX.put(PetLifeStage.WEIGHT_LOSS, weightLoss);
    }

    /**
     * 고정 계수 맵 생성 (모든 활동 수준에 동일한 계수 적용)
     * Create fixed factor map (same factor for all activity levels)
     */
    private static Map<PetActivityLevel, Double> createFixedFactorMap(double factor) {
        Map<PetActivityLevel, Double> map = new EnumMap<>(PetActivityLevel.class);
        for (PetActivityLevel level : PetActivityLevel.values()) {
            map.put(level, factor);
        }
        return map;
    }

    @Override
    public double calculateDailyCalories(Pet pet) {
        if (pet.getPetWeight() == null || pet.getPetWeight() <= 0) {
            throw new IllegalArgumentException("Pet weight is required for calorie calculation");
        }

        PetLifeStage lifeStage = pet.getLifeStage();
        PetActivityLevel activityLevel = pet.getActivityLevel();

        // 생애 단계가 없으면 자동 추정 / Estimate if not provided
        if (lifeStage == null) {
            lifeStage = estimateLifeStage(pet);
        }

        // 활동 수준 기본값 / Default activity level
        if (activityLevel == null) {
            activityLevel = PetActivityLevel.NORMAL;
        }

        return calculateDailyCalories(pet.getPetWeight(), lifeStage, activityLevel);
    }

    @Override
    public double calculateDailyCalories(double weightKg, PetLifeStage lifeStage, PetActivityLevel activityLevel) {
        // RER 계산 / Calculate RER
        double rer = calculateRER(weightKg);

        // 활동 계수 조회 / Get activity factor
        double factor = getActivityFactor(lifeStage, activityLevel);

        // MER 계산 / Calculate MER
        double mer = rer * factor;

        // 소수점 첫째 자리까지 반올림 / Round to 1 decimal place
        return Math.round(mer * 10.0) / 10.0;
    }

    /**
     * RER (기초대사량) 계산
     * Calculate Resting Energy Requirement
     *
     * @param weightKg 체중 (kg) / Weight in kg
     * @return RER (kcal/day)
     */
    private double calculateRER(double weightKg) {
        if (weightKg <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }
        // RER = 70 × (체중:Weight)^0.75
        return 70 * Math.pow(weightKg, 0.75);
    }


    /**
     * 생애 단계와 활동 수준 조합으로 활동 계수 조회
     * Get activity factor by life stage and activity level combination
     *
     * <br>
     * <br>| 생애 단계           | 비활동적 | 보통 | 활동적 | 매우 활동적 |
     * <br>|---------------------|----------|------|--------|-------------|
     * <br>| 퍼피(<4개월)        | 2.5      | 3.0  | 3.0    | 3.0         |
     * <br>| 퍼피(4~12개월)      | 1.8      | 2.0  | 2.2    | 2.2         |
     * <br>| 성견(중성화)        | 1.4      | 1.6  | 1.8    | 2.0         |
     * <br>| 성견(미중성화)      | 1.6      | 1.8  | 2.0    | 2.5         |
     * <br>| 시니어              | 1.2      | 1.4  | 1.6    | 1.8         |
     * <br>| 임신 후기           | 3.0 (고정)                              |
     * <br>| 수유 중             | 4.0 (고정)                              |
     * <br>| 체중 감량           | 1.0 (고정)                              |
     *
     * @param lifeStage     생애 단계 / Life stage
     * @param activityLevel 활동 수준 / Activity level
     * @return 활동 계수 / Activity factor
     */
    private double getActivityFactor(@NonNull PetLifeStage lifeStage, @NonNull PetActivityLevel activityLevel) {
        Map<PetActivityLevel, Double> activityMap = FACTOR_MATRIX.get(lifeStage);
        if (activityMap == null) {
            throw new IllegalArgumentException("Unknown life stage: " + lifeStage);
        }

        Double factor = activityMap.get(activityLevel);
        if (factor == null) {
            throw new IllegalArgumentException("Unknown activity level: " + activityLevel);
        }

        return factor;
    }

    public PetLifeStage estimateLifeStage(Pet pet) {
        Integer ageMonths = pet.getPetAge();
        Gender gender = pet.getPetGender();

        // 나이 기반 추정 / Age-based estimation
        if (ageMonths != null) {
            // 4개월 미만 퍼피 / Puppy under 4 months
            if (ageMonths < 4) {
                return PetLifeStage.PUPPY_UNDER_4_MONTHS;
            }
            // 4~12개월 퍼피 / Puppy 4-12 months
            if (ageMonths < 12) {
                return PetLifeStage.PUPPY_4_TO_12_MONTHS;
            }
            // 7세(84개월) 이상 시니어 / Senior 7+ years
            if (ageMonths >= 84) {
                return PetLifeStage.SENIOR;
            }
        }

        // 성별 기반 추정 (성견) / Gender-based estimation (adult)
        if (gender != null) {
            // 중성화 여부에 따라 / Based on neutering status
            if (gender == Gender.NEUTERED_MALE || gender == Gender.SPAYED_FEMALE) {
                return PetLifeStage.ADULT_NEUTERED;
            } else {
                return PetLifeStage.ADULT_INTACT;
            }
        }

        // 기본값: 중성화 성견 / Default: neutered adult
        return PetLifeStage.ADULT_NEUTERED;
    }

}
