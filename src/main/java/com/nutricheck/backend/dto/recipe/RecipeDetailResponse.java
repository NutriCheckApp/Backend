package com.nutricheck.backend.dto.recipe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDetailResponse {

    @JsonProperty("recipe_id")
    private Long recipeId;

    @JsonProperty("recipe_name")
    private String recipeName;

    private String description;

    private Double calories;

    @JsonProperty("CrudeProtein")
    private Double protein;

    @JsonProperty("CrudeFat")
    private Double fat;

    @JsonProperty("CrudeFiber")
    private Double fiber;

    private Double calcium;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty("recipe_ingredients")
    private List<RecipeIngredientResponse> ingredients;

    @JsonProperty("recipe_steps")
    private List<RecipeStepResponse> steps;
}
