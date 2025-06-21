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
public class ReviewRequest {
    private Integer id;

    @NotEmpty(message = "Nội dung bình luận không được để trống")
    private String comment;

    private int rating;

    @NotNull(message = "Mã khóa học không được để trống")
    @JsonProperty("course_id")
    private Integer courseId;
}