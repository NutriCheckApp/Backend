package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.CalendarEntry;
import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.calendar.CalendarEntryRequest;
import com.nutricheck.backend.dto.calendar.CalendarEntryResponse;
import com.nutricheck.backend.dto.calendar.FileMetadata;
import com.nutricheck.backend.exception.exception.CalendarEntryNotFoundException;
import com.nutricheck.backend.exception.exception.ImageNotExistsException;
import com.nutricheck.backend.repository.CalendarRepository;
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

    private final CalendarRepository calendarRepository;

    private final FileSaverService imageSaverService;

    @Transactional(readOnly = true)
    public CalendarEntryResponse getEntry(User user, LocalDate date) {
        CalendarEntry entry = calendarRepository
                .findByUserAndDate(user, date)
                .orElseThrow(() -> new CalendarEntryNotFoundException(date));

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

        CalendarEntry calendarEntry = calendarRepository
                .findByUserAndDate(user, request.getDate())
                .orElseGet(CalendarEntry::new);

        String oldImageUrl = calendarEntry.getImageUrl();
        log.info("oldImageUrl {}", oldImageUrl);

        // update calendar entry
        calendarEntry.setUser(user);
        calendarEntry.setImageName(fileMetadata.getFileName());
        calendarEntry.setImageUrl(fileMetadata.getFileUrl());
        calendarEntry.setMemo(request.getMemo());
        calendarEntry.setDate(request.getDate());

        CalendarEntry saved = calendarRepository.save(calendarEntry);

        log.info("Saved calendar entry: {}", saved);

        if (oldImageUrl != null && !oldImageUrl.isBlank()) {
            log.info("Deleting old image file: {}", oldImageUrl);
            imageSaverService.deleteFile(oldImageUrl);
        }

        return CalendarEntryResponse.builder()
                .date(saved.getDate())
                .memo(saved.getMemo())
                .imageName(saved.getImageName())
                .build();
    }

    @Transactional(readOnly = true)
    public Resource getFileResource(User user, String imageName) {

        // get url from db.
        // even if a photo exists, it will not be accessible if it is not in the database.
        CalendarEntry oldEntry = calendarRepository
                .findByUserAndImageName(user, imageName)
                .orElseThrow(() -> new CalendarEntryNotFoundException(imageName));

        log.info("Old entry: {}", oldEntry);

        if (oldEntry.getImageUrl() == null || oldEntry.getImageUrl().isBlank()) {
            throw new ImageNotExistsException();
        }
        return imageSaverService.getFile(oldEntry.getImageUrl());
    }

    @Transactional()
    public CalendarEntryResponse deleteImage(User user, String imageName) {

        // get url from db.
        // even if a photo exists, it will not be accessible if it is not in the database.
        CalendarEntry oldEntry = calendarRepository
                .findByUserAndImageName(user, imageName)
                .orElseThrow(() -> new CalendarEntryNotFoundException(imageName));

        log.info("Old entry: {}", oldEntry);
        if (oldEntry.getImageUrl() == null || oldEntry.getImageUrl().isBlank()) {
            throw new ImageNotExistsException();
        }
        imageSaverService.deleteFile(oldEntry.getImageUrl());

        oldEntry.setImageUrl(null);
        oldEntry.setImageName(null);
        CalendarEntry saved = calendarRepository.save(oldEntry);

        return CalendarEntryResponse.builder()
                .date(saved.getDate())
                .memo(saved.getMemo())
                .imageName(saved.getImageName())
                .build();
    }

}
