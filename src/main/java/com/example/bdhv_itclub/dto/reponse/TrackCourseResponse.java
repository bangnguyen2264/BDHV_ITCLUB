package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrackCourseResponse {

    private Integer id;

    @JsonProperty("lesson_id")
    private Integer lessonId;

    @JsonProperty("duration_video")
    private LocalTime durationVideo;

    @JsonProperty("is_completed")
    private boolean isCompleted;

    @JsonProperty("is_unlock")
    private boolean isUnlock;

    @JsonProperty("is_current")
    private boolean isCurrent;
}
