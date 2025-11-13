package com.nutricheck.backend.repository;

import com.nutricheck.backend.domain.Essential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 권장 영양소 리포지토리
 * Required nutrients repository
 */
@Repository
public interface EssentialRepository extends JpaRepository<Essential, Long> {

    /**
     * 연령대와 성별로 권장 영양소 조회
     * Find required nutrients by age range and gender
     */
    Optional<Essential> findByAgeRangeAndGender(String ageRange, String gender);
}