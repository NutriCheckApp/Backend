package com.nutricheck.backend.repository;

import com.nutricheck.backend.domain.WeeklyMealPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 주간 식단표 리포지토리
 * Weekly meal plan repository
 */
@Repository
public interface WeeklyMealPlanRepository extends JpaRepository<WeeklyMealPlan, Long> {

    /**
     * 사용자 ID와 주 시작일로 조회
     * Find meal plan by user ID and week start date
     */
    Optional<WeeklyMealPlan> findByUser_UserIdAndWeekStartDate(Long userId, LocalDate weekStartDate);

    /**
     * 사용자의 최근 식단표 조회
     * Find recent meal plans by user
     */
    @Query("SELECT w FROM WeeklyMealPlan w WHERE w.user.userId = :userId " +
            "ORDER BY w.weekStartDate DESC")
    List<WeeklyMealPlan> findRecentMealPlansByUserId(@Param("userId") Long userId);
}