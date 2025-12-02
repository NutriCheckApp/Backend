package com.nutricheck.backend.dto.recipe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeSummaryResponse {

    @JsonProperty("recipe_id")
    private Long recipeId;

    @JsonProperty("recipe_name")
    private String recipeName;

    @JsonProperty("image_name")
    private String imageName;

}