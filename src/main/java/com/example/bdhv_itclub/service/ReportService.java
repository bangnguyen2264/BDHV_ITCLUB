package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.reponse.*;

import java.util.List;

public interface ReportService {
    ReportCountResponse count();
    List<ReportRevenueResponse> getRevenueReport(String period);
    List<ReportRevenueResponse> getCategoryIncomeReport();
    List<ReportRevenueResponse> getCourseIncomeReport(String period);
    List<ReportContestResponse> getContestReport();
}