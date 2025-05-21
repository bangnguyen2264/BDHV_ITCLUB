package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListReviewResponse {

    @JsonProperty("list_responses")
    private List<ReviewResponse> listResponses = new ArrayList<>();

    @JsonProperty("average_review")
    private double averageReview;

    @JsonProperty("total_review")
    private int totalReview;
}
