package com.nutricheck.backend.dto.recipe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeStepResponse {

    private Long stepId;

    @JsonProperty("step_number")
    private Integer stepNumber;

    private String instruction;

    private String imageUrl;

    private Integer estimatedTime;
}

