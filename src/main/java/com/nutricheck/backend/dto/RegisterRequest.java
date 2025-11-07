package com.nutricheck.backend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Name required")
    private String name;

    @NotBlank(message = "Email required")
    private String email;

    @NotBlank(message = "Password required")
    private String password;

    @Positive( message = "Age must be a positive number")
    private int age;

    @NotBlank(message = "Gender cannot be empty")
    private String gender;

}
