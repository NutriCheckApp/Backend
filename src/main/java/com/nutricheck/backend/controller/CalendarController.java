package com.nutricheck.backend.controller;

import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.calendar.CalendarEntryRequest;
import com.nutricheck.backend.dto.calendar.CalendarEntryResponse;
import com.nutricheck.backend.repository.UserRepository;
import com.nutricheck.backend.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam("date") String date
    ) {
        User resolved = resolveUser(user);
        return calendarService.getEntry(resolved, date);
    }

    // 저장/수정
    @PostMapping
    public CalendarEntryResponse saveEntry(
            @AuthenticationPrincipal User user,
            @RequestBody CalendarEntryRequest request
    ) {
        User resolved = resolveUser(user);
        return calendarService.saveOrUpdateEntry(resolved, request);
    }
}
