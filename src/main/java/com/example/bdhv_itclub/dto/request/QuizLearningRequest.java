package com.example.bdhv_itclub.dto.request;

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
    @NotNull(message = "Mã câu hỏi không được để trống")
    @JsonProperty("quiz_id")
    private Integer id;

    @JsonProperty("answers")
    @Valid
    @NotEmpty(message = "Các câu trả lời không được để trống")
    private List<QuizAnswerLearningRequest> answers = new ArrayList<>();
}