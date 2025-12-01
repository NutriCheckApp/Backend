package com.nutricheck.backend.controller;

import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.calendar.CalendarEntryRequest;
import com.nutricheck.backend.dto.calendar.CalendarEntryResponse;
import com.nutricheck.backend.service.CalendarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    // 특정 날짜 조회
    @GetMapping
    public ResponseEntity<CalendarEntryResponse> getEntry(
            @AuthenticationPrincipal User user,
            @RequestParam("date") @DateTimeFormat(pattern = "YYYY-MM-DD") LocalDate date) {
        CalendarEntryResponse entry = calendarService.getEntry(user, date);
        log.info(entry.toString());
        return ResponseEntity.ok(entry);
    }

    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CalendarEntryResponse> uploadPhoto(
            @Valid @ModelAttribute CalendarEntryRequest request,
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        CalendarEntryResponse calendarEntryResponse = calendarService.saveImage(user, file, request);
        return ResponseEntity.ok(calendarEntryResponse);
    }

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@AuthenticationPrincipal User user,
                                             @PathVariable String filename) {
        log.info("filename.toString()" + filename);
        log.info("user.toString()" + user.toString());

        Resource resource = calendarService.getFileResource(user, filename);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @DeleteMapping("/image/{filename:.+}")
    public ResponseEntity<CalendarEntryResponse> deleteImage(@AuthenticationPrincipal User user,
                                            @PathVariable String filename) {
        log.info("filename.toString()" + filename);
        log.info("user.toString()" + user.toString());

        CalendarEntryResponse entry  = calendarService.deleteImage(user, filename);
        return ResponseEntity.ok(entry);
    }
}
