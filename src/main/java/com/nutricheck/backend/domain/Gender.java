package com.nutricheck.backend.domain;

import lombok.Getter;

/**
 * 반려견 성별 열거형
 * Pet gender enumeration
 */
@Getter
public enum Gender {
    /**
     * 수컷
     * Male
     */
    MALE("수컷"),

    /**
     * 암컷
     * Female
     */
    FEMALE("암컷"),

    /**
     * 중성화 수컷
     * Neutered male
     */
    NEUTERED_MALE("중성화 수컷"),

    /**
     * 중성화 암컷
     * Spayed female
     */
    SPAYED_FEMALE("중성화 암컷");

    private final String description;

    Gender(String description) {
        this.description = description;
    }
}