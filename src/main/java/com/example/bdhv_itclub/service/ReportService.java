package com.example.bdhv_itclub.service;

import com.example.bdhv_itclub.dto.reponse.ContestReportResponse;
import com.example.bdhv_itclub.dto.reponse.CountReportResponse;
import com.example.bdhv_itclub.dto.reponse.RevenueReportResponse;

import java.util.List;

public interface ReportService {
    List<RevenueReportResponse> getRevenueReport(String period);

    List<RevenueReportResponse> getCategoryIncomeReport();

    List<RevenueReportResponse> getCourseIncomeReport(String period);

    List<ContestReportResponse> getContestReport();

    CountReportResponse count();
}
