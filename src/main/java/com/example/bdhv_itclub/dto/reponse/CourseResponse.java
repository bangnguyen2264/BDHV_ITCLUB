package com.example.bdhv_itclub.dto.reponse;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "title", "slug", "description", "thumbnail", "price", "discount", "student_count", "published_at", "is_enabled", "is_published", "category"})
public class CourseResponse {

    private Integer id;

    private String title;

    private String slug;

    private String description;

    private String thumbnail;

    private int price;

    private float discount;

    @JsonProperty("student_count")
    private int studentCount;

    @JsonProperty("published_at")
    private Instant publishedAt;

    @JsonProperty("is_enabled")
    private boolean isEnabled;

    @JsonProperty("is_published")
    private boolean isPublished;

    @JsonProperty("is_finished")
    private boolean isFinished;

    @JsonProperty("total_review")
    private int totalReview;

    @JsonProperty("average_review")
    private double averageReview;

    private CategoryDTO category;

    @JsonProperty("info_list")
    private List<CourseInfoResponse> infoList;

    @JsonProperty("chapter_list")
    private List<ChapterDTO> chapterList;

    @JsonProperty("total_chapter")
    private int totalChapter;

    @JsonProperty("total_lesson")
    private int totalLesson;
}
