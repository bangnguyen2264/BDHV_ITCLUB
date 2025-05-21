package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.ListReviewResponse;
import com.example.bdhv_itclub.dto.request.ReviewRequest;
import com.example.bdhv_itclub.service.ReviewService;
import com.example.bdhv_itclub.utils.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/create")
    @ApiMessage("Create a review")
    public ResponseEntity<?> create(@RequestBody @Valid ReviewRequest reviewRequest){
        return ResponseEntity.ok(reviewService.createReview(reviewRequest));
    }

    @DeleteMapping("/delete/{id}")
    @ApiMessage("Delete the review")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Integer reviewId){
        return ResponseEntity.ok(reviewService.deleteReview(reviewId));
    }

    @GetMapping("/get-all/course/{id}")
    @ApiMessage("Get all reviews by course id")
    public ResponseEntity<?> listByCourse(@PathVariable(value = "id") Integer courseId){
        ListReviewResponse listReviewResponse = reviewService.listAllByCourse(courseId);
        if(listReviewResponse.getListResponses().isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listReviewResponse);
    }

    @GetMapping("/get-all")
    @ApiMessage("List all reviews")
    public ResponseEntity<?> list(){
        ListReviewResponse listReviewResponse = reviewService.listAll();
        if(listReviewResponse.getListResponses().isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listReviewResponse);
    }

    @GetMapping("/check-reviewed/user/{user_id}/course/{course_id}")
    public ResponseEntity<?> checkReviewed(@PathVariable(value = "user_id") Integer userId,
                                           @PathVariable(value = "course_id") Integer courseId){
        return ResponseEntity.ok(reviewService.checkCustomerToReviewed(userId, courseId));
    }
}
