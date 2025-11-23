package com.nutricheck.backend.dto;

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
    private String petName;

    /**
     * 반려견 품종 / petBreed
     */
    private String petBreed;

    /**
     * 반려견 크기 (소형, 중형, 대형) / petSize (small, medium, large)
     */
    private String petSize;

    /**
     * 반려견 무게 (kg) / petWeight
     */
    @NotNull(message = "Pet weight is required")
    @Positive(message = "Pet weight must be positive")
    private Double petWeight;

    /**
     * 반려견 성별 / petGender
     */
    @NotNull(message = "Pet gender is required")
    private Gender petGender;

    /**
     * 반려견 나이 (개월 수) / petAge (in months)
     */
    @NotNull(message = "Pet age is required")
    @Positive(message = "Pet age must be positive")
    private Integer petAge;

    /**
     * 생애 단계 (선택, 미입력시 자동 추정) / lifeStage (optional, auto-estimated if not provided)
     */
    private PetLifeStage lifeStage;

    /**
     * 활동 수준 (선택, 기본값: NORMAL) / activityLevel (optional, default: NORMAL)
     */
    private PetActivityLevel activityLevel;
}
