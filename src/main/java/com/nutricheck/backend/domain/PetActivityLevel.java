package com.nutricheck.backend.domain;

import lombok.Getter;

/**
 * 반려견 활동 수준 열거형
 * Pet activity level enumeration
 *
 * 순수 활동량만 정의합니다. 생애 단계는 PetLifeStage에서 관리합니다.
 * Defines pure activity levels only. Life stages are managed in PetLifeStage.
 */
@Getter
public enum PetActivityLevel {

    /**
     * 비활동적 / 실내 생활
     * Inactive / indoor lifestyle
     */
    INACTIVE("비활동적"),

    /**
     * 보통 / 일반적인 활동량
     * Normal / typical activity
     */
    NORMAL("보통"),

    /**
     * 활동적 / 매일 산책 및 운동
     * Active / daily walks and exercise
     */
    ACTIVE("활동적"),

    /**
     * 매우 활동적 / 작업견, 스포츠견
     * Very active / working dog, sport dog
     */
    VERY_ACTIVE("매우 활동적");

    private final String description;

    PetActivityLevel(String description) {
        this.description = description;
    }
}