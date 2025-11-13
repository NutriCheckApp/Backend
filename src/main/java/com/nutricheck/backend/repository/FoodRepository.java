package com.nutricheck.backend.repository;

import com.nutricheck.backend.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 식품 리포지토리
 * Food repository
 */
@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    /**
     * 식품명으로 검색 (부분 일치)
     * Search foods by name (partial match)
     */
    List<Food> findByFoodNameContaining(String foodName);

    /**
     * 식품명으로 검색 (정확히 일치)
     * Find food by exact name
     */
    List<Food> findByFoodName(String foodName);
}