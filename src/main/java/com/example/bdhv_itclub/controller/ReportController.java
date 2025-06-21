package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.ReportCountResponse;
import com.example.bdhv_itclub.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/count")
    public ResponseEntity<ReportCountResponse> count() {
        return ResponseEntity.ok(reportService.count());
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/sales-income/{period}")
    public ResponseEntity<?> getRevenueReport(
            @PathVariable(value = "period") String period
    ) {
        return ResponseEntity.ok().body(reportService.getRevenueReport(period));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/category-income")
    public ResponseEntity<?> getCategoryIncomeReport() {
        return ResponseEntity.ok().body(reportService.getCategoryIncomeReport());
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/course-income/{period}")
    public ResponseEntity<?> getCourseIncomeReport(
            @PathVariable(value = "period") String period
    ) {
        return ResponseEntity.ok().body(reportService.getCourseIncomeReport(period));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/contest-report")
    public ResponseEntity<?> getContestReport() {
        return ResponseEntity.ok().body(reportService.getContestReport());
    }
}