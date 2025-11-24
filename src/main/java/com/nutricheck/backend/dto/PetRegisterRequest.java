package com.nutricheck.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nutricheck.backend.domain.Gender;
import com.nutricheck.backend.domain.PetActivityLevel;
import com.nutricheck.backend.domain.PetLifeStage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 반려견 등록 요청 DTO
 * Pet registration request DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetRegisterRequest {

    /**
     * 반려견 이름 / petName
     */
    @NotBlank(message = "Pet name cannot be empty")
    @JsonProperty("pet_name")
    private String petName;

    /**
     * 반려견 품종 / petBreed
     */
    @JsonProperty("pet_breed")
    private String petBreed;

    /**
     * 반려견 크기 (소형, 중형, 대형) / petSize (small, medium, large)
     */
    @JsonProperty("pet_size")
    private String petSize;

    /**
     * 반려견 무게 (kg) / petWeight
     */
    @NotNull(message = "Pet weight is required")
    @Positive(message = "Pet weight must be positive")
    @JsonProperty("pet_weight")
    private Double petWeight;

    /**
     * 반려견 성별 / petGender
     */
    @NotNull(message = "Pet gender is required")
    @JsonProperty("pet_gender")
    private Gender petGender;

    /**
     * 반려견 나이 (개월 수) / petAge (in months)
     */
    @NotNull(message = "Pet age is required")
    @Positive(message = "Pet age must be positive")
    @JsonProperty("pet_age")
    private Integer petAge;

    /**
     * 생애 단계 (선택, 미입력시 자동 추정) / lifeStage (optional, auto-estimated if not provided)
     */
    @JsonProperty("life_stage")
    private PetLifeStage lifeStage;

    /**
     * 활동 수준 (선택, 기본값: NORMAL) / activityLevel (optional, default: NORMAL)
     */
    @JsonProperty("activity_level")
    private PetActivityLevel activityLevel;
}
