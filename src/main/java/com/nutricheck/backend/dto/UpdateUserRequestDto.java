
package com.nutricheck.backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDto {

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 50)
    private String name;

    @Positive( message = "Age must be a positive number")
    private int age;

    @NotBlank(message = "Gender cannot be empty")
    private String gender;

    @Positive(message = "Weight must be a positive number")
    private BigDecimal weight;

    @Positive(message = "Height must be a positive number")
    private BigDecimal height;

    @NotBlank(message = "Activity level cannot be empty")
    private String activity_level;

    @Positive(message = "Goal weight must be a positive number")
    private double goal_weight;

}
