package com.nutricheck.backend.dto.recipe;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredientResponse {

    private Long ingredientId;
    private String ingredientName;
    private Double amount;
    private String unit;
    private Integer displayOrder;
}