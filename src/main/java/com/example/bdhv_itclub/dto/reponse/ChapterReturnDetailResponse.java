package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChapterReturnDetailResponse {

    private Integer id;

    private String name;

    @JsonProperty("total_lesson")
    private int totalLesson;

    @JsonProperty("duration_chapter")
    private LocalTime durationChapter;

    @JsonProperty("lesson_list")
    private List<LessonReturnDetailResponse> lessonList;

    private int orders;
}
