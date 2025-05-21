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
public class LessonRequestInQuiz {

    @JsonProperty("lesson_id")
    @NotNull(message = "Lesson id can not be null")
    private Integer id;

    @JsonProperty("list_quizzes")
    @Valid
    @NotEmpty(message = "List quiz can not be empty")
    private List<QuizLearningRequest> listQuizzes = new ArrayList<>();
}
