package com.nutricheck.backend.dto.calendar;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CalendarEntryResponse {

    private Long id;
    private String date;
    private String memo;
    private String imageUrl;
}