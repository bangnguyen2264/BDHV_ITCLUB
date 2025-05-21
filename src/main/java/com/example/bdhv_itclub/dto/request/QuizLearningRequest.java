package com.example.bdhv_itclub.dto.request;

import com.example.bdhv_itclub.dto.reponse.AnswerLearningRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class QuizLearningRequest {

    @NotNull(message = "Quiz id can not be null")
    @JsonProperty("quiz_id")
    private Integer id;

    @JsonProperty("list_answers")
    @Valid
    @NotEmpty(message = "List answer can not be empty")
    private List<AnswerLearningRequest> listAnswers = new ArrayList<>();
}
