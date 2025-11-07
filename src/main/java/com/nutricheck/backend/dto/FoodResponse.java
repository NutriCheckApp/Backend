package com.nutricheck.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodResponse {

    private Long food_id;
    private String food_name;
    private BigDecimal calories;
    private BigDecimal carbohydrate;
    private BigDecimal protein;
    private BigDecimal sat_fat;
    private BigDecimal unsat_fat;
    private BigDecimal trans_fat;
    private BigDecimal sodium;
    private BigDecimal sugar;
    private BigDecimal cholesterol;
    private BigDecimal calcium;
}
