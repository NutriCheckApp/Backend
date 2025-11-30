package com.nutricheck.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nutricheck.backend.domain.Gender;
import com.nutricheck.backend.domain.PetActivityLevel;
import com.nutricheck.backend.domain.PetLifeStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 반려견 정보 응답 DTO
 * Pet information response DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetInfoResponse {

    /**
     * 반려견 ID / petId
     */
    @JsonProperty("pet_id")
    private Long petId;

    /**
     * 반려견 이름 / petName
     */
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
    @JsonProperty("pet_weight")
    private Double petWeight;

    /**
     * 반려견 성별 / petGender
     */
    @JsonProperty("pet_gender")
    private Gender petGender;

    /**
     * 반려견 나이 (개월 수) / petAge (in months)
     */
    @JsonProperty("pet_age")
    private Integer petAge;

    /**
     * 생애 단계 / lifeStage
     */
    @JsonProperty("life_stage")
    private PetLifeStage lifeStage;

    /**
     * 활동 수준 / activityLevel
     */
    @JsonProperty("activity_level")
    private PetActivityLevel activityLevel;

    /**
     * 하루 권장 칼로리 (kcal) / dailyCalories (kcal/day)
     */
    @JsonProperty("daily_calories")
    private Double dailyCalories;
}
