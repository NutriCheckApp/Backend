package com.nutricheck.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 건강 정보 업데이트 응답 DTO
 * Update user metrics response DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserMetricsResponse {

    /**
     * 성공 메시지
     * Success message
     */
    private String message;

    /**
     * 기초대사량 (Basal Metabolic Rate)
     * BMR in kcal
     */
    private Double bmr;

    /**
     * 활동대사량 (Total Daily Energy Expenditure)
     * TDEE in kcal
     */
    private Double tdee;
}