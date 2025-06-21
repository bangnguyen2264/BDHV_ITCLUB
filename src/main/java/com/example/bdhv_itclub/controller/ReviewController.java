package com.example.bdhv_itclub.controller;


import com.example.bdhv_itclub.dto.reponse.ReviewListResponse;
import com.example.bdhv_itclub.dto.request.ReviewRequest;
import com.example.bdhv_itclub.service.ReviewService;
import com.example.bdhv_itclub.utils.annotation.APIResponseMessage;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get-all")
    @APIResponseMessage("Liệt kê tất cả đánh giá")
    public ResponseEntity<?> listAll() {
        ReviewListResponse reviewsResponse = reviewService.listAll();
        if(reviewsResponse.getReviewResponses().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reviewsResponse);
    }

    // Ok
    @GetMapping("/get-all/course/{id}")
    @APIResponseMessage("Lấy tất cả đánh giá theo mã khóa học")
    public ResponseEntity<?> listByCourse(
            @PathVariable(value = "id") Integer courseId
    ) {
        ReviewListResponse reviewsResponse = reviewService.listAllByCourse(courseId);
        if(reviewsResponse.getReviewResponses().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reviewsResponse);
    }

    // Ok
    @PostMapping("/create")
    @APIResponseMessage("Thêm đánh giá")
    public ResponseEntity<?> create(
            @RequestBody @Valid ReviewRequest reviewRequest,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(reviewService.createReview(reviewRequest, email));
    }

    // Ok
    @DeleteMapping("/delete/{id}")
    @APIResponseMessage("Xóa đánh giá")
    public ResponseEntity<?> delete(
            @PathVariable(value = "id") Integer reviewId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(reviewService.deleteReview(reviewId, email));
    }

    // Ok
    @GetMapping("/check-reviewed/user/{user_id}/course/{course_id}")
    public ResponseEntity<?> checkReviewed(
            @PathVariable(value = "user_id") Integer userId,
            @PathVariable(value = "course_id") Integer courseId
    ) {
        return ResponseEntity.ok(reviewService.checkReviewedByCourse(userId, courseId));
    }
}