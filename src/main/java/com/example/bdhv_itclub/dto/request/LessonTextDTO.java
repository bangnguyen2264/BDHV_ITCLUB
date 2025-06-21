package com.example.bdhv_itclub.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LessonTextDTO {
    private Integer id;

    @NotEmpty(message = "Nội dung mô tả bài học không được để trống")
    private String content;
}