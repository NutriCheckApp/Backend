package com.nutricheck.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    private String jwt;

    private String message;

    private String username;

    private double dailyCalories;

    private double dailyCrudeProtein;

    private double dailyCrudeFat;

    private double dailyCrudeFibers;

    private double dailyCalcium;

}
