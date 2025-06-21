package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseForHomePage {
    private Integer id;

    private String title;

    private String slug;

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

    @JsonProperty("total_review")
    private int totalReview;

    @JsonProperty("average_review")
    private double averageReview;
}