package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.Food;
import com.nutricheck.backend.domain.Intake;
import com.nutricheck.backend.domain.MealType;
import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.DiaryLogRequest;
import com.nutricheck.backend.dto.DiaryLogResponse;
import com.nutricheck.backend.repository.FoodRepository;
import com.nutricheck.backend.repository.IntakeRepository;
import com.nutricheck.backend.repository.MealTypeRepository;
import com.nutricheck.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 식단 기록 서비스 구현체
 * Diary service implementation
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryServiceImpl implements DiaryService {

    private final IntakeRepository intakeRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final MealTypeRepository mealTypeRepository;

    /**
     * 날짜별 식단 기록 조회
     * Get diary logs by date
     */
    @Override
    public List<DiaryLogResponse> getDiaryLogsByDate(Long userId, LocalDate date) {
        List<Intake> intakes = intakeRepository.findByUser_UserIdAndIntakeDate(userId, date);

        return intakes.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 식단 기록 추가
     * Add diary log
     */
    @Override
    @Transactional
    public DiaryLogResponse addDiaryLog(Long userId, DiaryLogRequest request) {
        // 사용자 조회 / Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 식품 조회 / Find food
        Food food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new RuntimeException("Food not found"));

        // 식사 시간대 조회 / Find meal type
        MealType mealType = mealTypeRepository.findByMealName(request.getMealType())
                .orElseThrow(() -> new RuntimeException("Meal type not found"));

        // 섭취 기록 생성 / Create intake record
        Intake intake = Intake.builder()
                .user(user)
                .food(food)
                .mealType(mealType)
                .intakeAmount(request.getIntakeAmount())
                .intakeDate(request.getIntakeDate() != null ? request.getIntakeDate() : LocalDate.now())
                .intakeTime(LocalDateTime.now())
                .build();

        Intake savedIntake = intakeRepository.save(intake);

        return convertToResponse(savedIntake);
    }

    /**
     * 식단 기록 수정
     * Update diary log
     */
    @Override
    @Transactional
    public DiaryLogResponse updateDiaryLog(Long userId, Long intakeId, DiaryLogRequest request) {
        // 섭취 기록 조회 및 권한 확인 / Find intake and verify ownership
        Intake intake = intakeRepository.findById(intakeId)
                .orElseThrow(() -> new RuntimeException("Intake not found"));

        if (!intake.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        // 식품 조회 / Find food
        Food food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new RuntimeException("Food not found"));

        // 식사 시간대 조회 / Find meal type
        MealType mealType = mealTypeRepository.findByMealName(request.getMealType())
                .orElseThrow(() -> new RuntimeException("Meal type not found"));

        // 섭취 기록 업데이트 / Update intake record
        intake.setFood(food);
        intake.setMealType(mealType);
        intake.setIntakeAmount(request.getIntakeAmount());
        if (request.getIntakeDate() != null) {
            intake.setIntakeDate(request.getIntakeDate());
        }

        return convertToResponse(intake);
    }

    /**
     * 식단 기록 삭제
     * Delete diary log
     */
    @Override
    @Transactional
    public void deleteDiaryLog(Long userId, Long intakeId) {
        // 섭취 기록 조회 및 권한 확인 / Find intake and verify ownership
        Intake intake = intakeRepository.findById(intakeId)
                .orElseThrow(() -> new RuntimeException("Intake not found"));

        if (!intake.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        intakeRepository.delete(intake);
    }

    /**
     * Intake 엔티티를 DiaryLogResponse로 변환
     * Convert Intake entity to DiaryLogResponse
     */
    private DiaryLogResponse convertToResponse(Intake intake) {
        Food food = intake.getFood();
        Double ratio = intake.getIntakeAmount() / food.getServingSize();

        return DiaryLogResponse.builder()
                .intakeId(intake.getIntakeId())
                .foodId(food.getFoodId())
                .foodName(food.getFoodName())
                .intakeAmount(intake.getIntakeAmount())
                .intakeDate(intake.getIntakeDate())
                .intakeTime(intake.getIntakeTime())
                .mealType(intake.getMealType().getMealName())
                .mealTypeDisplayName(intake.getMealType().getDisplayName())
                // 섭취량에 비례한 영양소 계산 / Calculate nutrients based on intake amount
                .calories(food.getCalories() * ratio)
                .carbohydrate(food.getCarbohydrate() * ratio)
                .protein(food.getProtein() * ratio)
                .fat(food.getFat() * ratio)
                .build();
    }
}