package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountReportResponse {
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

    @JsonProperty("total_orders")
    private int totalOrders;

    @JsonProperty("total_reviews")
    private int totalReviews;

    @JsonProperty("total_incomes")
    private int totalIncomes;
}
