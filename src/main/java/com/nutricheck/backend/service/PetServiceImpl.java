package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.*;
import com.nutricheck.backend.dto.PetInfoResponse;
import com.nutricheck.backend.dto.PetRegisterRequest;
import com.nutricheck.backend.dto.PetUpdateRequest;
import com.nutricheck.backend.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 반려견 서비스 구현체
 * Pet service implementation
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    // ========================================
    // 활동 계수 매트릭스 / Activity Factor Matrix
    // ========================================

    /**
     * 생애 단계 × 활동 수준 조합 활동 계수 테이블
     * Life stage × Activity level combination factor table
     *
     * | 생애 단계           | 비활동적 | 보통 | 활동적 | 매우 활동적 |
     * |---------------------|----------|------|--------|-------------|
     * | 퍼피(<4개월)        | 2.5      | 3.0  | 3.0    | 3.0         |
     * | 퍼피(4~12개월)      | 1.8      | 2.0  | 2.2    | 2.2         |
     * | 성견(중성화)        | 1.4      | 1.6  | 1.8    | 2.0         |
     * | 성견(미중성화)      | 1.6      | 1.8  | 2.0    | 2.5         |
     * | 시니어              | 1.2      | 1.4  | 1.6    | 1.8         |
     * | 임신 후기           | 3.0 (고정)                              |
     * | 수유 중             | 4.0 (고정)                              |
     * | 체중 감량           | 1.0 (고정)                              |
     */
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

    // ========================================
    // CRUD 메서드 / CRUD Methods
    // ========================================

    /**
     * 반려견 등록 (칼로리 자동 계산)
     * Register a new pet with automatic calorie calculation
     */
    @Override
    public PetInfoResponse registerPet(User user, PetRegisterRequest request) {
        Pet petEntity = modelMapper.map(request, Pet.class);
        petEntity.setUser(user);

        // 생애 단계가 없으면 자동 추정 / Estimate life stage if not provided
        if (petEntity.getLifeStage() == null) {
            petEntity.setLifeStage(estimateLifeStage(petEntity));
        }

        // 활동 수준 기본값 설정 / Set default activity level
        if (petEntity.getActivityLevel() == null) {
            petEntity.setActivityLevel(PetActivityLevel.NORMAL);
        }

        // 하루 권장 칼로리 계산 / Calculate daily recommended calories
        double dailyCalories = calculateDailyCalories(petEntity);
        petEntity.setDailyCalories(dailyCalories);

        Pet created = petRepository.save(petEntity);
        return modelMapper.map(created, PetInfoResponse.class);
    }

    /**
     * ID로 반려견 조회
     * Get pet by ID
     */
    @Override
    public PetInfoResponse getPetById(User user, Long petId) {
        Pet pet = getPet(user, petId);
        return modelMapper.map(pet, PetInfoResponse.class);
    }

    /**
     * 사용자의 모든 반려견 조회
     * Get all pets by user
     */
    @Override
    public List<PetInfoResponse> getPetsByUser(User user) {
        List<Pet> pets = petRepository.findByUser(user);
        return pets.stream()
                .map(pet -> modelMapper.map(pet, PetInfoResponse.class))
                .toList();
    }

    /**
     * 반려견 정보 수정 (칼로리 자동 재계산)
     * Update pet information with automatic calorie recalculation
     */
    @Override
    public PetInfoResponse updatePet(User user, Long petId, PetUpdateRequest updateRequest) {
        Pet pet = getPet(user, petId);
        modelMapper.map(updateRequest, pet);

        // 생애 단계가 없으면 자동 추정 / Estimate life stage if not provided
        if (pet.getLifeStage() == null) {
            pet.setLifeStage(estimateLifeStage(pet));
        }

        // 활동 수준 기본값 설정 / Set default activity level
        if (pet.getActivityLevel() == null) {
            pet.setActivityLevel(PetActivityLevel.NORMAL);
        }

        // 하루 권장 칼로리 재계산 / Recalculate daily recommended calories
        double dailyCalories = calculateDailyCalories(pet);
        pet.setDailyCalories(dailyCalories);

        petRepository.save(pet);
        return modelMapper.map(pet, PetInfoResponse.class);
    }

    /**
     * 반려견 삭제
     * Delete pet
     */
    @Override
    public void deletePet(User user, Long petId) {
        // 존재 여부 및 소유권 확인 / Check existence and ownership
        Pet pet = getPet(user, petId);
        petRepository.deleteById(petId);
    }

    // ========================================
    // 칼로리 계산 메서드 / Calorie Calculation Methods
    // ========================================

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

    /**
     * 체중, 생애 단계, 활동 수준으로 하루 권장 칼로리 계산
     * Calculate daily calories by weight, life stage, and activity level
     *
     * @param weightKg      체중 (kg) / Weight in kg
     * @param lifeStage     생애 단계 / Life stage
     * @param activityLevel 활동 수준 / Activity level
     * @return 하루 권장 칼로리 (kcal/day) / Daily recommended calories
     */
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
     * @param lifeStage     생애 단계 / Life stage
     * @param activityLevel 활동 수준 / Activity level
     * @return 활동 계수 / Activity factor
     */
    private double getActivityFactor(PetLifeStage lifeStage, PetActivityLevel activityLevel) {
        if (lifeStage == null) {
            throw new IllegalArgumentException("Life stage is required");
        }
        if (activityLevel == null) {
            throw new IllegalArgumentException("Activity level is required");
        }

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

    // ========================================
    // 추정 메서드 / Estimation Methods
    // ========================================

    /**
     * 나이와 성별 기반 생애 단계 추정
     * Estimate life stage based on age and gender
     *
     * @param pet 반려견 엔티티 / Pet entity
     * @return 추정된 생애 단계 / Estimated life stage
     */
    private PetLifeStage estimateLifeStage(Pet pet) {
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

    /**
     * 사용자 소유 반려견 조회 (내부용)
     * Get pet owned by user (internal use)
     */
    private Pet getPet(User user, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet with id " + petId + " not found!"));

        if (!pet.getUser().equals(user)) {
            throw new EntityNotFoundException("Pet with id " + petId + " does not belong to the user!");
        }
        return pet;
    }
}