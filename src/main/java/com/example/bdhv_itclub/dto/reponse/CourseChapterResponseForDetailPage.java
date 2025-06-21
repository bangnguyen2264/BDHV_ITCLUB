package com.example.bdhv_itclub.dto.reponse;

import com.example.bdhv_itclub.dto.reponse.LessonResponseForDetailPage;
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
public class CourseChapterResponseForDetailPage {
    private Integer id;

    private String name;

    @JsonProperty("total_lesson")
    private int totalLesson;

    @JsonProperty("chapter_duration")
    private LocalTime chapterDuration;

    @JsonProperty("lessons")
    private List<LessonResponseForDetailPage> lessons;

    @JsonProperty("chapter_order")
    private int chapterOrder;
}