package com.nutricheck.backend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiaryLogRequest {

    @Positive
    private BigDecimal intake_amount;

    private LocalDate intake_date;

    @NotBlank
    private String meal_type;
}
