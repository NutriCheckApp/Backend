package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.*;
import com.nutricheck.backend.dto.recipe.*;
import com.nutricheck.backend.exception.exception.InvalidImageUrlException;
import com.nutricheck.backend.repository.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final ImageSaverService imageSaverService;

    /**
     * 메인 화면용 레시피 리스트 (최대 12개)
     * 레시피 id, 이름, 이미지, 칼로리 정도만 내려줌
     */
    public List<RecipeSummaryResponse> getRecipeListForMain() {
        // createdAt 기준으로 최근 것부터 12개 (필요에 따라 정렬 기준 바꿔도 됨)
        PageRequest pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.DESC, "createdAt"));
        return recipeRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * 레시피 상세 조회
     */
    public RecipeDetailResponse getRecipeDetail(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("레시피를 찾을 수 없습니다. id=" + recipeId));
        return toDetailResponse(recipe);
    }

    // ======= 내부 매핑 메서드들 =======

    private RecipeSummaryResponse toSummaryResponse(Recipe recipe) {
        return RecipeSummaryResponse.builder()
                .recipeId(recipe.getRecipeId())
                .recipeName(recipe.getRecipeName())
                .imageName(recipe.getImageName())
                .build();
    }

    private RecipeDetailResponse toDetailResponse(Recipe recipe) {
        // 재료는 displayOrder 기준 정렬
        List<RecipeIngredientResponse> ingredientResponses = recipe.getIngredients()
                .stream()
                .sorted(Comparator.comparing(
                        ingredient -> ingredient.getDisplayOrder() != null ? ingredient.getDisplayOrder() : 0
                ))
                .map(this::toIngredientResponse)
                .collect(Collectors.toList());

        // 조리 단계는 stepNumber 기준 정렬
        List<RecipeStepResponse> stepResponses = recipe.getSteps()
                .stream()
                .sorted(Comparator.comparing(
                        step -> step.getStepNumber() != null ? step.getStepNumber() : 0
                ))
                .map(this::toStepResponse)
                .collect(Collectors.toList());

        return RecipeDetailResponse.builder()
                .recipeId(recipe.getRecipeId())
                .recipeName(recipe.getRecipeName())
                .description(recipe.getDescription())
                .calories(recipe.getCalories())
                .protein(recipe.getProtein())
                .fat(recipe.getFat())
                .fiber(recipe.getFiber())
                .calcium(recipe.getCalcium())
                .imageName(recipe.getImageName())
                .ingredients(ingredientResponses)
                .steps(stepResponses)
                .build();
    }

    private RecipeIngredientResponse toIngredientResponse(RecipeIngredient ingredient) {
        return RecipeIngredientResponse.builder()
                .ingredientId(ingredient.getIngredientId())
                .ingredientName(ingredient.getIngredientName())
                .amount(ingredient.getAmount())
                .unit(ingredient.getUnit())
                .displayOrder(ingredient.getDisplayOrder())
                .build();
    }

    private RecipeStepResponse toStepResponse(RecipeStep step) {
        return RecipeStepResponse.builder()
                .stepId(step.getStepId())
                .stepNumber(step.getStepNumber())
                .instruction(step.getInstruction())
                .imageUrl(step.getImageUrl())
                .estimatedTime(step.getEstimatedTime())
                .build();
    }

    public Resource getImage(String filename) {

        Recipe recipeByImageName = recipeRepository.getRecipeByImageName(filename)
                .orElseThrow(InvalidImageUrlException::new);

        log.info("recipeByImageName: {}", recipeByImageName);

        if (recipeByImageName.getImageUrl() == null || recipeByImageName.getImageUrl().isBlank()) {
            throw new InvalidImageUrlException();
        }
        return imageSaverService.getFile(recipeByImageName.getImageUrl());
    }
}
