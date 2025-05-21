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
public class CourseReturnLearningPageResponse {
    private Integer id;

    private String title;

    @JsonProperty("chapter_list")
    private List<ChapterReturnDetailResponse> chapterList;

    @JsonProperty("total_lesson")
    private int totalLesson;

    @JsonProperty("is_finished")
    private boolean isFinished;
}
