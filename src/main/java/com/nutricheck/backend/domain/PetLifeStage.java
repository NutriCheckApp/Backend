package com.nutricheck.backend.domain;

import lombok.Getter;

/**
 * 반려견 생애 단계 열거형
 * Pet life stage enumeration
 *
 * 나이, 중성화 여부, 특수 상태에 따른 생애 단계를 정의합니다.
 * Defines life stages based on age, neutering status, and special conditions.
 */
@Getter
public enum PetLifeStage {

    /**
     * 퍼피 (4개월 미만)
     * Puppy (under 4 months)
     */
    PUPPY_UNDER_4_MONTHS("퍼피 (4개월 미만)"),

    /**
     * 퍼피 (4~12개월)
     * Puppy (4-12 months)
     */
    PUPPY_4_TO_12_MONTHS("퍼피 (4~12개월)"),

    /**
     * 성견 (중성화)
     * Adult (neutered)
     */
    ADULT_NEUTERED("성견 (중성화)"),

    /**
     * 성견 (미중성화)
     * Adult (intact)
     */
    ADULT_INTACT("성견 (미중성화)"),

    /**
     * 시니어 (7세 이상)
     * Senior (7+ years)
     */
    SENIOR("시니어"),

    /**
     * 임신 후기
     * Late pregnancy
     */
    PREGNANT_LATE("임신 후기"),

    /**
     * 수유 중
     * Lactating
     */
    LACTATING("수유 중"),

    /**
     * 체중 감량 필요
     * Weight loss needed
     */
    WEIGHT_LOSS("체중 감량");

    private final String description;

    PetLifeStage(String description) {
        this.description = description;
    }
}