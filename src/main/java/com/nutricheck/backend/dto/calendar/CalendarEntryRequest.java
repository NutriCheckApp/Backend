package com.nutricheck.backend.dto.calendar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarEntryRequest {

    // "2025-11-25" 형식 (YYYY-MM-DD)
    private String date;
    private String memo;
    private String imageUrl;   // 나중에 실제 파일 업로드로 확장해도 됨
}
