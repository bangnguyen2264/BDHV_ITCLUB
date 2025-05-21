package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContestReportResponse {
    private String identifier;

    @JsonProperty("average_grade")
    private float averageGrade;

    @JsonProperty("number_of_joined")
    private int numberOfJoined;

    public ContestReportResponse(String identifier) {
        this.identifier = identifier;
    }
}
