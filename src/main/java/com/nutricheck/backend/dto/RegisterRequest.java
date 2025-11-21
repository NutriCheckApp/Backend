package com.nutricheck.backend.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * 회원가입 요청 DTO
 * User registration request DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    /**
     * 사용자 이름 (로그인 ID)
     * Username for login
     */
    @NotBlank(message = "Username required")
    private String username;

    /**
     * 사용자 실명
     * User's real name
     */
    @NotBlank(message = "Name required")
    private String name;

    /**
     * 비밀번호
     * Password
     */
    @NotBlank(message = "Password required")
    private String password;

    /**
     * 이메일
     * Email address
     */
    @NotBlank(message = "Email required")
    @Email(message = "Invalid email format")
    private String email;

    /**
     * 나이
     * Age
     */
    @NotNull(message = "Age required")
    @Positive(message = "Age must be a positive number")
    private Integer age;

    /**
     * 성별 (M: 남성, F: 여성)
     * Gender (M: Male, F: Female)
     */
    @NotBlank(message = "Gender required")
    private String gender;

    /**
     * 키 (cm)
     * Height in centimeters
     */
    @Positive(message = "Height must be positive")
    private Double height;

    /**
     * 현재 체중 (kg)
     * Current weight in kilograms
     */
    @Positive(message = "Weight must be positive")
    private Double weight;

    /**
     * 목표 체중 (kg)
     * Goal weight in kilograms
     */
    private Double goalWeight;

    /**
     * 목표 유형 (DIET, GAIN, MAINTAIN)
     * Goal type
     */
    private String goalType;

    /**
     * 활동 수준
     * Activity level
     */
    private String activityLevel;
}
