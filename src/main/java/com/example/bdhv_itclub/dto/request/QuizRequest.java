package com.example.bdhv_itclub.dto.request;

import com.example.bdhv_itclub.dto.reponse.AnswerDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizRequest {

    private Integer id;

    @NotEmpty(message = "Question can not be empty")
    @Length(min = 10, max = 200, message = "Question must have 10 - 200 characters")
    private String question;

    @NotEmpty
    @Pattern(
            regexp = "^(ONE_CHOICE|MULTIPLE_CHOICE|PERFORATE)$",
            message = "Question info type must be one of: one choice , multiple choice, perforate"
    )
    @JsonProperty("quiz_type")
    private String quizType;

    @JsonProperty("lesson_id")
    private Integer lessonId;

    @JsonProperty("answer_list")
    @Valid
    @NotEmpty(message = "List answer can not be empty")
    private List<AnswerDto> answerList = new ArrayList<>();
}
