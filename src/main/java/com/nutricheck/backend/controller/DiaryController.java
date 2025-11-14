package com.nutricheck.backend.controller;

import com.nutricheck.backend.dto.DiaryLogRequest;
import com.nutricheck.backend.dto.DiaryLogResponse;
import com.nutricheck.backend.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 식단 기록 컨트롤러
 * Diary controller for managing food intake records
 */
@Tag(name = "Diary", description = "식단 기록 API / Diary API")
@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    /**
     * 날짜별 식단 기록 조회
     * Get diary logs by date
     */
    @Operation(summary = "날짜별 식단 기록 조회", description = "특정 날짜의 식단 기록을 조회합니다")
    @GetMapping()
    public ResponseEntity<List<DiaryLogResponse>> getDiaryLogByDate(
            @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        // TODO: get user from Auth Context (현재는 임시로 userId=1 사용)
        // In production, get userId from authentication context
        Long userId = 1L;
        List<DiaryLogResponse> logs = diaryService.getDiaryLogsByDate(userId, date);
        return ResponseEntity.ok(logs);
    }

    /**
     * 식단 기록 추가
     * Add diary log
     */
    @Operation(summary = "식단 기록 추가", description = "새로운 식단 기록을 추가합니다")
    @PostMapping("/log")
    public ResponseEntity<DiaryLogResponse> addDiaryLog(@RequestBody @Valid DiaryLogRequest request) {
        // TODO: get user from Auth Context (현재는 임시로 userId=1 사용)
        // In production, get userId from authentication context
        Long userId = 1L;
        DiaryLogResponse response = diaryService.addDiaryLog(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 식단 기록 수정
     * Update diary log
     */
    @Operation(summary = "식단 기록 수정", description = "기존 식단 기록을 수정합니다")
    @PutMapping("/log/{intake_id}")
    public ResponseEntity<DiaryLogResponse> updateDiaryLog(
            @PathVariable("intake_id") Long intakeId,
            @RequestBody @Valid DiaryLogRequest request) {
        // TODO: get user from Auth Context (현재는 임시로 userId=1 사용)
        // In production, get userId from authentication context
        Long userId = 1L;
        DiaryLogResponse response = diaryService.updateDiaryLog(userId, intakeId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 식단 기록 삭제
     * Delete diary log
     */
    @Operation(summary = "식단 기록 삭제", description = "식단 기록을 삭제합니다")
    @DeleteMapping("/log/{intake_id}")
    public ResponseEntity<Void> deleteDiaryLog(@PathVariable("intake_id") Long intakeId) {
        // TODO: get user from Auth Context (현재는 임시로 userId=1 사용)
        // In production, get userId from authentication context
        Long userId = 1L;
        diaryService.deleteDiaryLog(userId, intakeId);
        return ResponseEntity.noContent().build();
    }
}
