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
public class ContestResponseForTest {
    private Integer id;

    private String title;

    private int period;

    @JsonProperty("created_at")
    private Instant createdAt;

    private boolean enabled;

    @JsonProperty("number_of_question")
    private int numberOfQuestion;

    @JsonProperty("quizzes")
    private List<QuizResponseForLearningPage> quizzes = new ArrayList<>();
}