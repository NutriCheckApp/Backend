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
public class ProfileResponseDto {

    private Long user_id;

    private String name;

    private int age;

    private String gender;

    private BigDecimal weight;

    private BigDecimal height;

    private String activity_level;

    private BigDecimal bmr;

    private BigDecimal tdee;

    private BigDecimal goal_weight;

}
