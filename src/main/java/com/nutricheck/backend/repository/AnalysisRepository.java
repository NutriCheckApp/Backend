package com.nutricheck.backend.repository;

import com.nutricheck.backend.domain.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 일일 영양 분석 리포지토리
 * Daily analysis repository
 */
@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {

    /**
     * 사용자 ID와 분석 날짜로 조회
     * Find analysis by user ID and analysis date
     */
    Optional<Analysis> findByUser_UserIdAndAnalysisDate(Long userId, LocalDate analysisDate);

    /**
     * 사용자의 최근 분석 결과 조회
     * Find recent analyses by user
     */
    @Query("SELECT a FROM Analysis a WHERE a.user.userId = :userId " +
            "ORDER BY a.analysisDate DESC")
    List<Analysis> findRecentAnalysesByUserId(@Param("userId") Long userId);

    /**
     * 사용자의 특정 기간 분석 결과 조회
     * Find analyses by user within date range
     */
    @Query("SELECT a FROM Analysis a WHERE a.user.userId = :userId " +
            "AND a.analysisDate BETWEEN :startDate AND :endDate " +
            "ORDER BY a.analysisDate DESC")
    List<Analysis> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}