package com.nutricheck.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보 수정 요청 DTO
 * User profile update request DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    /**
     * 사용자 실명
     * User's real name
     */
    @NotBlank(message = "Name cannot be empty")
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;

    /**
     * 이메일
     * Email address
     */
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String email;
}
