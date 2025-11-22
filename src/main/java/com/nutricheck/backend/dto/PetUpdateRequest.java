package com.nutricheck.backend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetUpdateRequest {

    @NotBlank
    private String petName;

    private String petBreed;

    private String petColor;

    private String petSize;

    @Positive
    private int petAge;

}
