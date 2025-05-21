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
public class TrackCourseRequest {

    @NotNull(message = "Lesson id can not be null")
    @JsonProperty("lesson_id")
    private Integer lessonId;

    @NotNull(message = "User id can not be null")
    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("period_current")
    private LocalTime periodCurrent;
}
