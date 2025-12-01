package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.CalendarEntry;
import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.calendar.CalendarEntryRequest;
import com.nutricheck.backend.dto.calendar.CalendarEntryResponse;
import com.nutricheck.backend.dto.calendar.FileMetadata;
import com.nutricheck.backend.repository.CalendarEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarEntryRepository calendarEntryRepository;

    private final FileSaverService imageSaverService;

    @Transactional(readOnly = true)
    public CalendarEntryResponse getEntry(User user, LocalDate date) {
        CalendarEntry entry = calendarEntryRepository
                .findByUserAndDate(user, date)
                .orElseThrow(() -> new RuntimeException("Calendar Entry not found."));

        return CalendarEntryResponse.builder()
                .date(entry.getDate())
                .memo(entry.getMemo())
                .imageName(entry.getImageName())
                .build();
    }

    @Transactional
    public CalendarEntryResponse saveImage(User user,
                                           MultipartFile file,
                                           CalendarEntryRequest request) {
        FileMetadata fileMetadata = imageSaverService.saveFile(user.getUsername(), file);

//        CalendarEntry entry = CalendarEntry.builder()
//                .user(user)
//                .imageUrl(fileMetadata.getFileUrl())
//                .imageName(fileMetadata.getFileName())
//                .memo(request.getMemo())
//                .date(request.getDate())
//                .build();

        // todo: check prev photo and if it exists, delete it
        CalendarEntry calendarEntry = calendarEntryRepository
                .findByUserAndDate(user, request.getDate())
                .orElseGet(CalendarEntry::new);

        calendarEntry.setUser(user);
        calendarEntry.setImageName(fileMetadata.getFileName());
        calendarEntry.setImageUrl(fileMetadata.getFileUrl());
        calendarEntry.setMemo(request.getMemo());
        calendarEntry.setDate(request.getDate());

        CalendarEntry saved = calendarEntryRepository.save(calendarEntry);

        log.info("Saved calendar entry: {}", saved);

        return CalendarEntryResponse.builder()
                .date(saved.getDate())
                .memo(saved.getMemo())
                .imageName(saved.getImageName())
                .build();
    }

    @Transactional(readOnly = true)
    public Resource getFileResource(User user, String filename) {

        // get url from db.
        // even if a photo exists, it will not be accessible if it is not in the database.
        CalendarEntry oldEntry = calendarEntryRepository
                .findByUserAndImageName(user, filename)
                .orElseThrow(() -> new RuntimeException("Calendar Entry not found for a filename: " + filename));

        log.info("Old entry: {}", oldEntry);

        return imageSaverService.getFile(oldEntry.getImageUrl());
    }
}
