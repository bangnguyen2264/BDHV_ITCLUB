package com.example.bdhv_itclub.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseTrackingRequest {
    @NotNull(message = "Mã bài học không được để trống")
    @JsonProperty("lesson_id")
    private Integer lessonId;

    @NotNull(message = "Mã người dùng không được để trống")
    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("current_period")
    private LocalTime currentPeriod;
}