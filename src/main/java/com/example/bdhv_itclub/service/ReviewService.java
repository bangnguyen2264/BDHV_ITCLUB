package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.reponse.ListReviewResponse;
import com.example.bdhv_itclub.dto.reponse.ReviewResponse;
import com.example.bdhv_itclub.dto.request.ReviewRequest;

public interface ReviewService {
    ListReviewResponse listAllByCourse(Integer courseId);

    String checkCustomerToReviewed(Integer userId, Integer courseId);

    ReviewResponse createReview(ReviewRequest reviewRequest);

    String deleteReview(Integer reviewId);

    ListReviewResponse listAll();
}
