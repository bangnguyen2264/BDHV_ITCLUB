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
public class RecordRequest {
    @NotNull(message = "Mã cuộc thi không được để trống")
    @JsonProperty("contest_id")
    private Integer contestId;

    private int period;

    @JsonProperty("quizzes")
    @Valid
    @NotEmpty(message = "Danh sách câu hỏi không được để trống")
    private List<QuizLearningRequest> quizzes = new ArrayList<>();
}