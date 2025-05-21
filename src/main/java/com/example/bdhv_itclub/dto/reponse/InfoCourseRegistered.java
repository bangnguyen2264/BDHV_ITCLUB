package com.example.bdhv_itclub.dto.reponse;

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
public class InfoCourseRegistered {

    @JsonProperty("list_tracks")
    private List<TrackCourseResponse> listTracks;

    @JsonProperty("percent_achieved")
    private int percentAchieved;

    @JsonProperty("total_lesson_learned")
    private int totalLessonLearned;

    @JsonProperty("certificate_id")
    private Integer certificateId;
}
