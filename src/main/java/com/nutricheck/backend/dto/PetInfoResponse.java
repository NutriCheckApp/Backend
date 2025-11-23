package com.nutricheck.backend.dto;

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
    private Long petId;

    /**
     * 반려견 이름 / petName
     */
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
    private Double petWeight;

    /**
     * 반려견 성별 / petGender
     */
    private Gender petGender;

    /**
     * 반려견 나이 (개월 수) / petAge (in months)
     */
    private Integer petAge;

    /**
     * 생애 단계 / lifeStage
     */
    private PetLifeStage lifeStage;

    /**
     * 활동 수준 / activityLevel
     */
    private PetActivityLevel activityLevel;

    /**
     * 하루 권장 칼로리 (kcal) / dailyCalories (kcal/day)
     */
    private Double dailyCalories;
}
