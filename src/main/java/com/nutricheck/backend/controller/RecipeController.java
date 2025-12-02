package com.nutricheck.backend.controller;

import com.nutricheck.backend.dto.recipe.RecipeDetailRequest;
import com.nutricheck.backend.dto.recipe.RecipeDetailResponse;
import com.nutricheck.backend.dto.recipe.RecipeSummaryResponse;
import com.nutricheck.backend.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
    public RecipeDetailResponse getRecipeDetail(@RequestHeader String host,
                                                @RequestBody RecipeDetailRequest request) {
        return recipeService.getRecipeDetail(request.getRecipeId());
    }

    /**
     * (선택) RESTful path variable 버전
     * GET /api/v1/recipes/{recipeId}
     */
    @GetMapping("/{recipeId}")
    public RecipeDetailResponse getRecipeDetailByPath(@RequestHeader String host,
                                                      @PathVariable Long recipeId) {
        return recipeService.getRecipeDetail(recipeId);
    }

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource resource = recipeService.getImage( filename);
        if (resource == null) {
            log.info("resource == null" );
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
