package com.nutricheck.backend.service;

import com.nutricheck.backend.dto.DiaryLogRequest;
import com.nutricheck.backend.dto.DiaryLogResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * 식단 기록 서비스 인터페이스
 * Diary service interface
 */
public interface DiaryService {

    /**
     * 날짜별 식단 기록 조회
     * Get diary logs by date
     */
    List<DiaryLogResponse> getDiaryLogsByDate(Long userId, LocalDate date);

    /**
     * 식단 기록 추가
     * Add diary log
     */
    DiaryLogResponse addDiaryLog(Long userId, DiaryLogRequest request);

    /**
     * 식단 기록 수정
     * Update diary log
     */
    DiaryLogResponse updateDiaryLog(Long userId, Long intakeId, DiaryLogRequest request);

    /**
     * 식단 기록 삭제
     * Delete diary log
     */
    void deleteDiaryLog(Long userId, Long intakeId);
}