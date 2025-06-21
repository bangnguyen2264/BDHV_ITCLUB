package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecordResponseForReview {
    @JsonProperty("record_id")
    private Integer id;

    @JsonProperty("contest_title")
    private String contestTitle;

    @JsonProperty("joined_at")
    private Instant joinedAt;

    private float grade;

    private int period;

    @JsonProperty("total_quizzes")
    private float totalQuizzes;

    @JsonProperty("total_correct_quizzes")
    private float totalCorrectQuizzes;

    @JsonProperty("quizzes")
    private List<QuizResponseForRecord> quizzes = new ArrayList<>();
}