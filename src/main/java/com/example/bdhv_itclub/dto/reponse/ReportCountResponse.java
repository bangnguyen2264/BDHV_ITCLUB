package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportCountResponse {
    @JsonProperty("total_users")
    private int totalUsers;

    @JsonProperty("total_categories")
    private int totalCategories;

    @JsonProperty("total_courses")
    private int totalCourses;

    @JsonProperty("total_quizzes")
    private int totalQuizzes;

    @JsonProperty("total_blogs")
    private int totalBlogs;

    @JsonProperty("total_enrollments")
    private int totalEnrollments;

    @JsonProperty("total_reviews")
    private int totalReviews;
}
