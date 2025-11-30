package com.nutricheck.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 사용자 프로필 응답 DTO
 * User profile response DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {

    /**
     * 사용자 이름 (로그인 ID)
     * Username for login
     */
    private String username;

    /**
     * 사용자 실명
     * User's real name
     */
    private String name;

    /**
     * 이메일
     * Email address
     */
    private String email;

    @JsonProperty("pet_list")
    List<PetInfoResponse> pets;
}
