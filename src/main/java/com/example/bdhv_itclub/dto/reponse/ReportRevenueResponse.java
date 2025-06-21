package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReportRevenueResponse {
    private String identifier;

    @JsonProperty("total_income")
    private int totalIncome = 0;

    @JsonProperty("enrollment_count")
    private int enrollmentCount;

    public ReportRevenueResponse(String identifier) {
        this.identifier = identifier;
    }
}