package com.nutricheck.backend.dto.recipe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDetailRequest {

    @JsonProperty("recipe_id")
    private Long recipeId;
}

