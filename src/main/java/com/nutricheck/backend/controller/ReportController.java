package com.nutricheck.backend.controller;

import com.nutricheck.backend.dto.DailyReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    // todo: inj service

    @GetMapping("/daily/{date}")
    public ResponseEntity<DailyReportResponse> getDailyReport(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        // todo: generate report
        return ResponseEntity.ok(new DailyReportResponse());
    }

}
