package com.nutricheck.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetInfoResponse {

    private Long petId;

    private String petName;

    private String petBreed;

    private String petColor;

    private String petSize;

//    private String getOwnerUsername;

    private int petAge;

}
