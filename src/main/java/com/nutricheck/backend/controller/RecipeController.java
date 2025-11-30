package com.nutricheck.backend.controller;

import com.nutricheck.backend.dto.recipe.RecipeDetailRequest;
import com.nutricheck.backend.dto.recipe.RecipeDetailResponse;
import com.nutricheck.backend.dto.recipe.RecipeSummaryResponse;
import com.nutricheck.backend.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    /**
     * 레시피 메인 화면
     * 12개의 레시피: id, 이름, 이미지
     *
     * GET http://localhost:8080/api/v1/recipes
     */
    @GetMapping
    public List<RecipeSummaryResponse> getRecipeList() {
        return recipeService.getRecipeListForMain();
    }

    /**
     * 레시피 상세 조회
     *
     * POST http://localhost:8080/api/v1/recipes/detail
     *
     * Request Body:
     * {
     *   "recipe_id": 1
     * }
     */
    @PostMapping("/detail")
    public RecipeDetailResponse getRecipeDetail(@RequestBody RecipeDetailRequest request) {
        return recipeService.getRecipeDetail(request.getRecipeId());
    }

    /**
     * (선택) RESTful path variable 버전
     * GET /api/v1/recipes/{recipeId}
     */
    @GetMapping("/{recipeId}")
    public RecipeDetailResponse getRecipeDetailByPath(@PathVariable Long recipeId) {
        return recipeService.getRecipeDetail(recipeId);
    }
}
