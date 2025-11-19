
package com.nutricheck.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UpdateUserRequest {

    @NotBlank(message = "Username cannot be empty")
    @Size(max = 50)
    private String username;

    @Positive( message = "Age must be a positive number")
    private int age;

    @NotBlank(message = "Gender cannot be empty")
    private String gender;

    @Positive(message = "Weight must be a positive number")
    private BigDecimal weight;

    @Positive(message = "Height must be a positive number")
    private BigDecimal height;

    @JsonProperty("activity_level")
    @NotBlank(message = "Activity level cannot be empty")
    private String activityLevel;

    @JsonProperty("goal_weight")
    @Positive(message = "Goal weight must be a positive number")
    private BigDecimal goalWeight;
}
