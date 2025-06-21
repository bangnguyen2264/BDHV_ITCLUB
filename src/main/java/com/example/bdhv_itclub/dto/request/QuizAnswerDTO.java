package com.example.bdhv_itclub.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnswerDTO {
    private Integer id;

    @NotEmpty(message = "Nội dung câu trả lời không được để trống")
    @Length(min = 4, max = 200, message = "Nội dung câu trả lời phải từ 4-200 ký tự")
    private String content;

    @JsonProperty("is_correct")
    private boolean isCorrect;
}