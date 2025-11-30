package com.nutricheck.backend.controller;

import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.calendar.CalendarEntryRequest;
import com.nutricheck.backend.dto.calendar.CalendarEntryResponse;
import com.nutricheck.backend.repository.UserRepository;
import com.nutricheck.backend.service.CalendarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
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
    private final UserRepository userRepository;

    // application.yml 에서 가져오는 테스트모드 플래그
    // application.yml test-mode: true , false로 설정가능
    @Value("${calendar.test-mode:false}")
    private boolean isTestMode;

    // 테스트용 유저 로딩
    private User getTestUser() {
        return userRepository.findByUsername("testuser")
                .orElseThrow(() -> new IllegalStateException("테스트 유저(testuser)가 없습니다."));
    }

    // 목적: 둘 중 하나를 자동 선택하는 메서드
    private User resolveUser(User authenticatedUser) {
        if (isTestMode) {
            return getTestUser();
        }
        return authenticatedUser;   // 배포 모드에서는 JWT에서 받은 유저
    }

    // 특정 날짜 조회
    @GetMapping
    public CalendarEntryResponse getEntry(
            @AuthenticationPrincipal User user,
            @RequestParam("date") @DateTimeFormat(pattern = "YYYY-MM-DD") LocalDate date
    ) {
        User resolved = resolveUser(user);
        return calendarService.getEntry(resolved, date);
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

//    // 저장/수정
//    @PostMapping
//    public CalendarEntryResponse saveEntry(
//            @AuthenticationPrincipal User user,
//            @RequestBody CalendarEntryRequest request
//    ) {
//        User resolved = resolveUser(user);
//        return calendarService.saveOrUpdateEntry(resolved, request);
//    }
}
