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
public class BlogRequest {
    private Integer id;

    @NotEmpty(message = "Tiêu đề bài đăng không được để trống")
    private String title;

    @NotEmpty(message = "Mô tả bài đăng không được để trống")
    private String description;

    @NotEmpty(message = "Nội dung bài đăng không được để trống")
    private String content;
}