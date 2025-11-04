package com.nutricheck.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDto {

    private Long user_id;

    private String name;

    private int age;

    private String gender;

    private double weight;

    private double height;

    private String activity_level;

    private double bmr;

    private double tdee;

    private double goal_weight;

}
