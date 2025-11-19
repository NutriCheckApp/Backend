package com.nutricheck.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {

    private String username;

    private int age;

    private String gender;

    private BigDecimal weight;

    private BigDecimal height;

    @JsonProperty("activity_level")
    private String activityLevel;

    private BigDecimal bmr;

    private BigDecimal tdee;

    @JsonProperty("goal_weight")
    private BigDecimal goalWeight;

}
