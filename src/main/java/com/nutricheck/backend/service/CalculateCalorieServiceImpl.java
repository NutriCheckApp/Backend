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

        // 퍼피 (4~12개월, 중성화)
        Map<PetActivityLevel, Double> puppy4To12Neutered = new EnumMap<>(PetActivityLevel.class);
        puppy4To12Neutered.put(PetActivityLevel.INACTIVE, 1.5);
        puppy4To12Neutered.put(PetActivityLevel.NORMAL, 1.6);
        puppy4To12Neutered.put(PetActivityLevel.ACTIVE, 1.8);
        puppy4To12Neutered.put(PetActivityLevel.VERY_ACTIVE, 1.8);
        FACTOR_MATRIX.put(PetLifeStage.PUPPY_4_TO_12_MONTHS_NEUTERED, puppy4To12Neutered);

        // 퍼피 (4~12개월, 미중성화)
        Map<PetActivityLevel, Double> puppy4To12Intact = new EnumMap<>(PetActivityLevel.class);
        puppy4To12Intact.put(PetActivityLevel.INACTIVE, 1.8);
        puppy4To12Intact.put(PetActivityLevel.NORMAL, 2.0);
        puppy4To12Intact.put(PetActivityLevel.ACTIVE, 2.2);
        puppy4To12Intact.put(PetActivityLevel.VERY_ACTIVE, 2.2);
        FACTOR_MATRIX.put(PetLifeStage.PUPPY_4_TO_12_MONTHS_INTACT, puppy4To12Intact);

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

        // 시니어 (중성화)
        Map<PetActivityLevel, Double> seniorNeutered = new EnumMap<>(PetActivityLevel.class);
        seniorNeutered.put(PetActivityLevel.INACTIVE, 1.2);
        seniorNeutered.put(PetActivityLevel.NORMAL, 1.4);
        seniorNeutered.put(PetActivityLevel.ACTIVE, 1.6);
        seniorNeutered.put(PetActivityLevel.VERY_ACTIVE, 1.8);
        FACTOR_MATRIX.put(PetLifeStage.SENIOR_NEUTERED, seniorNeutered);

        // 시니어 (미중성화)
        Map<PetActivityLevel, Double> seniorIntact = new EnumMap<>(PetActivityLevel.class);
        seniorIntact.put(PetActivityLevel.INACTIVE, 1.4);
        seniorIntact.put(PetActivityLevel.NORMAL, 1.6);
        seniorIntact.put(PetActivityLevel.ACTIVE, 1.8);
        seniorIntact.put(PetActivityLevel.VERY_ACTIVE, 2.0);
        FACTOR_MATRIX.put(PetLifeStage.SENIOR_INTACT, seniorIntact);

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
     * <br>| 생애 단계              | 비활동적 | 보통 | 활동적 | 매우 활동적 |
     * <br>|------------------------|----------|------|--------|-------------|
     * <br>| 퍼피(<4개월)           | 2.5      | 3.0  | 3.0    | 3.0         |
     * <br>| 퍼피(4~12개월, 중성화) | 1.5      | 1.6  | 1.8    | 1.8         |
     * <br>| 퍼피(4~12개월, 미중성화)| 1.8     | 2.0  | 2.2    | 2.2         |
     * <br>| 성견(중성화)           | 1.4      | 1.6  | 1.8    | 2.0         |
     * <br>| 성견(미중성화)         | 1.6      | 1.8  | 2.0    | 2.5         |
     * <br>| 시니어(중성화)         | 1.2      | 1.4  | 1.6    | 1.8         |
     * <br>| 시니어(미중성화)       | 1.4      | 1.6  | 1.8    | 2.0         |
     * <br>| 임신 후기              | 3.0 (고정)                              |
     * <br>| 수유 중                | 4.0 (고정)                              |
     * <br>| 체중 감량              | 1.0 (고정)                              |
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

    /**
     * 반려견 정보로부터 생애 단계 추정
     * Estimate life stage from pet information
     *
     * @param pet 반려견 정보 / Pet information
     * @return 추정된 생애 단계 / Estimated life stage
     */
    public PetLifeStage estimateLifeStage(Pet pet) {
        Integer ageMonths = pet.getPetAge();
        boolean isNeutered = isNeutered(pet.getPetGender());

        // 나이가 없으면 성견으로 기본 처리 / Default to adult if age not provided
        if (ageMonths == null) {
            return isNeutered ? PetLifeStage.ADULT_NEUTERED : PetLifeStage.ADULT_INTACT;
        }

        // 4개월 미만 퍼피 (중성화 구분 없음) / Puppy under 4 months (no neutering distinction)
        if (ageMonths < 4) {
            return PetLifeStage.PUPPY_UNDER_4_MONTHS;
        }

        // 4~12개월 퍼피 (중성화 구분) / Puppy 4-12 months (neutering-based)
        if (ageMonths < 12) {
            return isNeutered ? PetLifeStage.PUPPY_4_TO_12_MONTHS_NEUTERED
                    : PetLifeStage.PUPPY_4_TO_12_MONTHS_INTACT;
        }

        // 7세(84개월) 이상 시니어 (중성화 구분) / Senior 7+ years (neutering-based)
        if (ageMonths >= 84) {
            return isNeutered ? PetLifeStage.SENIOR_NEUTERED : PetLifeStage.SENIOR_INTACT;
        }

        // 1~7세 성견 (중성화 구분) / Adult 1-7 years (neutering-based)
        return isNeutered ? PetLifeStage.ADULT_NEUTERED : PetLifeStage.ADULT_INTACT;
    }

    /**
     * 중성화 여부 판단
     * Check if pet is neutered
     *
     * @param gender 성별 / Gender
     * @return 중성화 여부 / Whether neutered
     */
    private boolean isNeutered(Gender gender) {
        return gender == Gender.NEUTERED_MALE || gender == Gender.SPAYED_FEMALE;
    }

    //---------하루 권장 영양성분 계산(APPO 기준, 임시) / Calculate Daily Recommended Nutrition(APPO, Temporally)-------

    @Override
    public double calculateDailyProtein(double dailyCalories) {
        if (dailyCalories <= 0) {
            throw new IllegalArgumentException("Daily calories must be positive");
        }
        // 칼로리의 25%를 단백질로 (1g = 4kcal)
        // 25% of calories from protein (1g = 4kcal)
        return Math.round((dailyCalories * 0.25 / 4.0) * 10.0) / 10.0;
    }

    @Override
    public double calculateDailyFat(double dailyCalories) {
        if (dailyCalories <= 0) {
            throw new IllegalArgumentException("Daily calories must be positive");
        }
        // 칼로리의 15%를 지방으로 (1g = 9kcal)
        // 15% of calories from fat (1g = 9kcal)
        return Math.round((dailyCalories * 0.15 / 9.0) * 10.0) / 10.0;
    }

    @Override
    public double calculateDailyFiber(double dailyCalories) {
        if (dailyCalories <= 0) {
            throw new IllegalArgumentException("Daily calories must be positive");
        }
        // 칼로리의 3%를 섬유로 (1g = 4kcal)
        // 3% of calories from fiber (1g = 4kcal)
        return Math.round((dailyCalories * 0.03 / 4.0) * 10.0) / 10.0;
    }

    @Override
    public double calculateDailyCalcium(double weightKg) {
        if (weightKg <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }
        // 체중 kg당 60mg
        // 60mg per kg of body weight
        return Math.round(weightKg * 60.0 * 10.0) / 10.0;
    }

}
