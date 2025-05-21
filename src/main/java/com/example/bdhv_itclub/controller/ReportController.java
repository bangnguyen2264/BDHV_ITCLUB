package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.CountReportResponse;
import com.example.bdhv_itclub.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/count")
    public ResponseEntity<CountReportResponse> count(){
        return ResponseEntity.ok(reportService.count());
    }

    @GetMapping("/sales-income/{period}")
    public ResponseEntity<?> getRevenueReport(@PathVariable(value = "period") String period){
        return ResponseEntity.ok().body(reportService.getRevenueReport(period));
    }
    @GetMapping("/category-income")
    public ResponseEntity<?> getCategoryIncomeReport(){
        return ResponseEntity.ok().body(reportService.getCategoryIncomeReport());
    }

    @GetMapping("/course-income/{period}")
    public ResponseEntity<?> getCourseIncomeReport(@PathVariable(value = "period") String period){
        return ResponseEntity.ok().body(reportService.getCourseIncomeReport(period));
    }

    @GetMapping("/contest-report")
    public ResponseEntity<?> getContestReport(){
        return ResponseEntity.ok().body(reportService.getContestReport());
    }
}
