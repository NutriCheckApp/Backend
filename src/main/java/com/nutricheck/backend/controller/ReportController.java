package com.nutricheck.backend.controller;

import com.nutricheck.backend.dto.DailyReportResponse;
import com.nutricheck.backend.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 영양 분석 리포트 컨트롤러
 * Nutritional analysis report controller
 */
@Tag(name = "Report", description = "영양 분석 리포트 API / Nutritional Analysis Report API")
@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * 일일 영양 분석 리포트 조회
     * Get daily nutritional analysis report
     */
    @Operation(
            summary = "일일 영양 분석 리포트 조회",
            description = "특정 날짜의 영양 분석 리포트를 조회합니다. 없으면 자동으로 생성합니다. (AI 분석 포함)"
    )
    @GetMapping("/daily/{date}")
    public ResponseEntity<DailyReportResponse> getDailyReport(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        // TODO: get user from Auth Context (현재는 임시로 userId=1 사용)
        // In production, get userId from authentication context
        Long userId = 1L;

        DailyReportResponse report = reportService.getDailyReport(userId, date);
        return ResponseEntity.ok(report);
    }

    /**
     * 일일 영양 분석 리포트 재생성
     * Regenerate daily nutritional analysis report
     */
    @Operation(
            summary = "일일 영양 분석 리포트 재생성",
            description = "특정 날짜의 영양 분석 리포트를 강제로 재생성합니다. 기존 데이터는 덮어씌워집니다."
    )
    @PostMapping("/daily/{date}/regenerate")
    public ResponseEntity<DailyReportResponse> regenerateDailyReport(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        // TODO: get user from Auth Context (현재는 임시로 userId=1 사용)
        // In production, get userId from authentication context
        Long userId = 1L;

        DailyReportResponse report = reportService.regenerateDailyReport(userId, date);
        return ResponseEntity.ok(report);
    }
}
