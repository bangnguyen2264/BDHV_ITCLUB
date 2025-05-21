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
public class CourseReturnSearch {

    private Integer id;

    private String title;

    private String slug;

    private String thumbnail;

    private String description;

    @JsonProperty("is_published")
    private boolean isPublished;

    @JsonProperty("published_at")
    private Instant publishedAt;

    @JsonProperty("average_review")
    private double averageReview;
}
