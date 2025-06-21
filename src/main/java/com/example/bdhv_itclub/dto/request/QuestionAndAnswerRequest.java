package com.example.bdhv_itclub.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAndAnswerRequest {
    private Integer id;

    @NotEmpty(message = "Nội dung không được để trống")
    private String content;

    @NotNull(message = "Mã bài học không được để trống")
    @JsonProperty("lesson_id")
    private Integer lessonId;

    @JsonProperty("parent_id")
    private Integer parentId;
}