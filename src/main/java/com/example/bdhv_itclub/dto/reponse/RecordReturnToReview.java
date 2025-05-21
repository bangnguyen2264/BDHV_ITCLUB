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
public class RecordReturnToReview {

    @JsonProperty("record_id")
    private Integer id;

    @JsonProperty("title_contest")
    private String titleContest;

    @JsonProperty("joined_at")
    private Instant joinedAt;

    private float grade;

    private int period;

    @JsonProperty("total_quizzes")
    private float totalQuizzes;

    @JsonProperty("total_quiz_is_correct")
    private float totalQuizIsCorrect;

    @JsonProperty("list_quizzes")
    private List<QuizReturnInRecord> listQuizzes = new ArrayList<>();

}
