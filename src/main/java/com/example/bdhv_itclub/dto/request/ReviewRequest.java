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

    @NotEmpty(message = "Comment can not be empty")
    private String comment;

    private int rating;

    @NotNull(message = "Course id can not be null")
    @JsonProperty("course_id")
    private Integer courseId;

    @NotNull(message = "User id can not be null")
    @JsonProperty("user_id")
    private Integer userId;
}
