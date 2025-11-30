package com.nutricheck.backend.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Username 중복 체크 응답 DTO
 * Username duplicate check response DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckUsernameResponse {

    /**
     * 중복 여부 (true: 중복됨, false: 사용 가능)
     * Duplicate status (true: exists, false: available)
     */
    private boolean exists;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecipeStepResponse {

        private Long stepId;

        @JsonProperty("step_number")
        private Integer stepNumber;

        private String instruction;

        private String imageUrl;

        private Integer estimatedTime;
    }
}