package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.CalendarEntry;
import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.calendar.CalendarEntryRequest;
import com.nutricheck.backend.dto.calendar.CalendarEntryResponse;
import com.nutricheck.backend.repository.CalendarEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarEntryRepository calendarEntryRepository;

    private final ImageSaverService imageSaverService;

    @Transactional(readOnly = true)
    public CalendarEntryResponse getEntry(User user, LocalDate date) {
        CalendarEntry entry = calendarEntryRepository
                .findByUserAndDate(user, date)
                .orElseThrow(() -> new RuntimeException("Calendar Entry not found."));

        return CalendarEntryResponse.builder()
                .id(entry.getId())
                .date(entry.getDate().toString())
                .memo(entry.getMemo())
                .imageUrl(entry.getImageUrl())
                .build();
    }

//    @Transactional
//    public CalendarEntryResponse saveOrUpdateEntry(User user, CalendarEntryRequest request) {
////        LocalDate date = LocalDate.parse(request.getDate());
//
//        CalendarEntry entry = calendarEntryRepository
//                .findByUserAndDate(user, request.getDate())
//                .orElseGet(() -> CalendarEntry.builder()
//                        .user(user)
//                        .date(request.getDate())
//                        .build());
//
//        entry.setMemo(request.getMemo());
//        entry.setImageUrl(request.getImageUrl());
//
//        CalendarEntry saved = calendarEntryRepository.save(entry);
//
//        return CalendarEntryResponse.builder()
//                .id(saved.getId())
//                .date(saved.getDate().toString())
//                .memo(saved.getMemo())
//                .imageUrl(saved.getImageUrl())
//                .build();
//    }

    @Transactional
    public CalendarEntryResponse saveImage(User user,
                                           MultipartFile file,
                                           CalendarEntryRequest request) {

        String imageUrl = imageSaverService.saveImage(user, file);

        CalendarEntry entry = calendarEntryRepository
                .findByUserAndDate(user, request.getDate())
                .orElseGet(() -> CalendarEntry.builder()
                        .user(user)
                        .imageUrl(imageUrl)
                        .memo(request.getMemo())
                        .date(request.getDate())
                        .build());

        CalendarEntry saved = calendarEntryRepository.save(entry);

        log.info(saved.toString());

        return CalendarEntryResponse.builder()
                .id(saved.getId())
                .date(saved.getDate().toString())
                .memo(saved.getMemo())
                .imageUrl(saved.getImageUrl())
                .build();
    }
}
