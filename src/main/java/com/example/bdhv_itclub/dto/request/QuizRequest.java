package com.example.bdhv_itclub.dto.request;

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

    @NotEmpty(message = "Nội dung câu hỏi không được để trống")
    @Length(min = 10, max = 200, message = "Nội dung câu hỏi phải từ 10-200 ký tự")
    private String question;

    @NotEmpty
    @Pattern(
            regexp = "^(ONE_CHOICE|MULTIPLE_CHOICE|PERFORATE)$",
            message = "Loại câu hỏi phải thuộc 1 trong 3 loại: 1 lựa chọn, nhiều lựa chọn hoặc đục lỗ"
    )
    @JsonProperty("quiz_type")
    private String quizType;

    @JsonProperty("lesson_id")
    private Integer lessonId;

    @JsonProperty("answers")
    @Valid
    @NotEmpty(message = "Danh sách câu trả lời không được để trống")
    private List<QuizAnswerDTO> answers = new ArrayList<>();
}