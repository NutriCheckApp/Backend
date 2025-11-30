package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.CalendarEntry;
import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.calendar.CalendarEntryRequest;
import com.nutricheck.backend.dto.calendar.CalendarEntryResponse;
import com.nutricheck.backend.repository.CalendarEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarEntryRepository calendarEntryRepository;

    @Transactional(readOnly = true)
    public CalendarEntryResponse getEntry(User user, String dateString) {
        LocalDate date = LocalDate.parse(dateString);

        CalendarEntry entry = calendarEntryRepository
                .findByUserAndDate(user, date)
                .orElse(null);

        if (entry == null) {
            return null;    // 프론트에서 "해당 날짜 기록 없음" 처리
        }

        return CalendarEntryResponse.builder()
                .id(entry.getId())
                .date(entry.getDate().toString())
                .memo(entry.getMemo())
                .imageUrl(entry.getImageUrl())
                .build();
    }

    @Transactional
    public CalendarEntryResponse saveOrUpdateEntry(User user, CalendarEntryRequest request) {
        LocalDate date = LocalDate.parse(request.getDate());

        CalendarEntry entry = calendarEntryRepository
                .findByUserAndDate(user, date)
                .orElseGet(() -> CalendarEntry.builder()
                        .user(user)
                        .date(date)
                        .build());

        entry.setMemo(request.getMemo());
        entry.setImageUrl(request.getImageUrl());

        CalendarEntry saved = calendarEntryRepository.save(entry);

        return CalendarEntryResponse.builder()
                .id(saved.getId())
                .date(saved.getDate().toString())
                .memo(saved.getMemo())
                .imageUrl(saved.getImageUrl())
                .build();
    }
}
