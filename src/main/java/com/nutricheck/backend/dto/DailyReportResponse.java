package com.nutricheck.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyReportResponse {

    private Long result_id;

    private Long user_id;

    private String carbohydrate_status;

    private String protein_status;

    private String sat_fat_status;

    private String unsat_fat_status;

    private String trans_fat_status;

    private String sodium_status;

    private String cholesterol_status;

    private String calcium_status;

    private String summary;
}
