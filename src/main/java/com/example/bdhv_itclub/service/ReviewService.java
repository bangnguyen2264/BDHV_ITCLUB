package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.reponse.ReviewListResponse;
import com.example.bdhv_itclub.dto.reponse.ReviewResponse;
import com.example.bdhv_itclub.dto.request.ReviewRequest;

public interface ReviewService {
    ReviewListResponse listAll();
    ReviewListResponse listAllByCourse(Integer courseId);
    ReviewResponse createReview(ReviewRequest reviewRequest, String email);
    String deleteReview(Integer reviewId, String email);
    String checkReviewedByCourse(Integer userId, Integer courseId);
}