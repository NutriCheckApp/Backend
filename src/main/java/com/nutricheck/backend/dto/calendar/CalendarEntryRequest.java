package com.nutricheck.backend.dto.calendar;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class CalendarEntryRequest {

    // "2025-11-25" 형식 (YYYY-MM-DD)
    @NotNull(message = "The date field cannot be null. (YYYY-MM-DD)")
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private LocalDate date;

    private String memo;
}
