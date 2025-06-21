package com.example.bdhv_itclub.dto.request;

import com.example.bdhv_itclub.dto.reponse.CourseTrackingResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRegisteredInformation {
    @JsonProperty("course_trackings")
    private List<CourseTrackingResponse> courseTrackings;

    @JsonProperty("achieved_percent")
    private int achievedPercent;

    @JsonProperty("total_lesson_learned")
    private int totalLessonLearned;

    @JsonProperty("certificate_id")
    private Integer certificateId;
}