package com.nutricheck.backend.repository;

import com.nutricheck.backend.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 레시피 리포지토리
 * Recipe repository
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    /**
     * 레시피 이름으로 검색
     * Find by recipe name containing keyword
     */
    List<Recipe> findByRecipeNameContaining(String recipeName);

    Optional<Recipe> getRecipeByImageName(String imageName);
}