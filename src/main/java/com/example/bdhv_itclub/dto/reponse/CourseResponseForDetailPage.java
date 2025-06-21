package com.example.bdhv_itclub.dto.reponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseForDetailPage {
    private Integer id;

    private String title;

    private String slug;

    private String thumbnail;

    private String description;

    @JsonProperty("published_at")
    private Instant publishedAt;

    private int price;

    private float discount;

    @JsonProperty("course_informations")
    private List<CourseInformationResponse> courseInformations;

    @JsonProperty("course_chapters")
    private List<CourseChapterResponseForDetailPage> courseChapters;

    @JsonProperty("total_chapter")
    private int totalChapter;

    @JsonProperty("total_lesson")
    private int totalLesson;

    @JsonProperty("total_time")
    private LocalTime totalTime;
}