package com.nutricheck.backend.service;

import com.nutricheck.backend.dto.DailyReportResponse;

import java.time.LocalDate;

/**
 * 영양 분석 리포트 서비스 인터페이스
 * Nutritional analysis report service interface
 */
public interface ReportService {

    /**
     * 일일 영양 분석 리포트 생성 및 조회
     * Generate and retrieve daily nutritional analysis report
     */
    DailyReportResponse getDailyReport(Long userId, LocalDate date);

    /**
     * 일일 영양 분석 리포트 재생성 (AI 분석 포함)
     * Regenerate daily report with AI analysis
     */
    DailyReportResponse regenerateDailyReport(Long userId, LocalDate date);
}