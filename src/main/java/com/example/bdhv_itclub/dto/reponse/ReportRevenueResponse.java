package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReportRevenueResponse {
    private String identifier; // Tên của bảng đang thống kê

    @JsonProperty("total_income")
    private int totalIncome = 0;

    @JsonProperty("order_count")
    private int orderCount;

    public ReportRevenueResponse(String identifier) {
        this.identifier = identifier;
    }
}
