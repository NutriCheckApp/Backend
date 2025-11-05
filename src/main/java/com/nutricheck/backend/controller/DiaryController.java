package com.nutricheck.backend.controller;

import com.nutricheck.backend.dto.DiaryLogRequest;
import com.nutricheck.backend.dto.DiaryLogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("diary")
@RequiredArgsConstructor
public class DiaryController {

    // todo: inject diary service

    @GetMapping()
    public ResponseEntity<List<DiaryLogResponse>> getDiaryLogByDate(
            @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        // todo: get user from Auth Context
        // todo: call service
        return ResponseEntity.ok(List.of(new DiaryLogResponse()));
    }

    @PostMapping("/log")
    public ResponseEntity<DiaryLogResponse> addDiaryLog(@RequestBody DiaryLogRequest request) {
        // todo: get user from Auth Context
        // todo: call service
        return ResponseEntity.status(HttpStatus.CREATED).body(new DiaryLogResponse());
    }

    @PutMapping("/log/{intake_id}")
    public ResponseEntity<DiaryLogResponse> updateDiaryLog(@PathVariable Long intake_id,
                                                             @RequestBody DiaryLogRequest request) {
        // todo: get user from Auth Context
        // todo: call service
        return ResponseEntity.ok(new DiaryLogResponse());
    }

    @DeleteMapping("/log/{intake_id}")
    public ResponseEntity<Void> deleteDiaryEntry(@PathVariable Long intake_id) {
        // todo: get user
        // todo: call service
         return ResponseEntity.noContent().build();
    }
}
