package com.nutricheck.backend.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 반려견 성별 열거형
 * Pet gender enumeration
 */
@Getter
public enum Gender {

    UNKNOWN("UNKNOWN", "UNKNOWN", "UNKNOWN"),

    /**
     * 수컷
     * Male
     */
    MALE("수컷", "MALE", "M"),

    /**
     * 암컷
     * Female
     */
    FEMALE("암컷", "FEMALE", "F"),

    /**
     * 중성화 수컷
     * Neutered male
     */
    NEUTERED_MALE("중성화 수컷", "NEUTERED_MALE", "NM"),

    /**
     * 중성화 암컷
     * Spayed female
     */
    SPAYED_FEMALE("중성화 암컷", "SPAYED_FEMALE", "SF");

    private final String englishDescription;

    private final String description;
    private final String shortDescription;

    Gender(String description, String englishDescription, String shortDescription) {
        this.description = description;
        this.englishDescription = englishDescription;
        this.shortDescription = shortDescription;
    }

    public static Optional<Gender> getGender(String text) {
        if (text == null || text.isEmpty()) {
            return Optional.empty();
        }
        return Arrays.stream(Gender.values())
                .filter(gender -> text.equalsIgnoreCase(gender.description)
                                  || text.equalsIgnoreCase(gender.englishDescription)
                                  || text.equalsIgnoreCase(gender.shortDescription))
                .findFirst();

    }
}