package com.nutricheck.backend.repository;

import com.nutricheck.backend.domain.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 식사 시간대 리포지토리
 * Meal type repository
 */
@Repository
public interface MealTypeRepository extends JpaRepository<MealType, Long> {

    /**
     * 식사 시간대 코드로 조회 (BREAKFAST, LUNCH, DINNER, SNACK)
     * Find meal type by meal name code
     */
    Optional<MealType> findByMealName(String mealName);
}