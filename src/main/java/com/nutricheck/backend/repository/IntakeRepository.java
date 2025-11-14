package com.nutricheck.backend.repository;

import com.nutricheck.backend.domain.Intake;
import com.nutricheck.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 식품 섭취 기록 리포지토리
 * Food intake repository
 */
@Repository
public interface IntakeRepository extends JpaRepository<Intake, Long> {

    /**
     * 사용자와 날짜로 섭취 기록 조회
     * Find intake records by user and date
     */
    List<Intake> findByUserAndIntakeDate(User user, LocalDate date);

    /**
     * 사용자 ID와 날짜로 섭취 기록 조회
     * Find intake records by user ID and date
     */
    List<Intake> findByUser_UserIdAndIntakeDate(Long userId, LocalDate date);

    /**
     * 사용자 ID, 날짜, 식사 시간대로 섭취 기록 조회
     * Find intake records by user ID, date, and meal type
     */
    List<Intake> findByUser_UserIdAndIntakeDateAndMealType_MealTypeId(
            Long userId, LocalDate date, Long mealTypeId);

    /**
     * 사용자의 특정 기간 섭취 기록 조회
     * Find intake records by user within date range
     */
    @Query("SELECT i FROM Intake i WHERE i.user.userId = :userId " +
            "AND i.intakeDate BETWEEN :startDate AND :endDate " +
            "ORDER BY i.intakeDate DESC, i.intakeTime DESC")
    List<Intake> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}