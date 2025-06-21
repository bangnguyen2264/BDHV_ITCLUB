package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecordResponse {
    private Integer id;

    private String username;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("contest_title")
    private String contestTitle;

    @JsonProperty("contest_id")
    private Integer contestId;

    @JsonProperty("joined_at")
    private Instant joinedAt;

    private float grade;

    private int period;

    @JsonProperty("total_quizzes")
    private float totalQuizzes;

    @JsonProperty("total_correct_quizzes")
    private float totalCorrectQuizzes;
}