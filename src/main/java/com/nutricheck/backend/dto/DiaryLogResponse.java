package com.nutricheck.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiaryLogResponse {

    private Long food_id;
    
    private double intake_amount;

    private LocalDate intake_date;

     private String meal_type;
}
