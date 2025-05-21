package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.request.FeedbackRequest;
import com.example.bdhv_itclub.dto.request.SendEmail;
import com.example.bdhv_itclub.service.FeedbackService;
import com.example.bdhv_itclub.utils.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendFeedback(@RequestBody @Valid FeedbackRequest feedbackRequest) {
        return new ResponseEntity<>(feedbackService.save(feedbackRequest), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    @ApiMessage("Get the feedback by id")
    public ResponseEntity<?> get(@PathVariable(value = "id") Integer feedbackId) {
        return ResponseEntity.ok(feedbackService.get(feedbackId));
    }

    @GetMapping("/list-all")
    @ApiMessage("List all feedbacks")
    public ResponseEntity<?> listALl() {
        return ResponseEntity.ok(feedbackService.listAll());
    }

    @DeleteMapping("/delete/{id}")
    @ApiMessage("Delete the feedback")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Integer feedbackId) {
        return ResponseEntity.ok(feedbackService.delete(feedbackId));
    }

    @PutMapping("/send-email")
    public ResponseEntity<?> sendEmail(@RequestBody @Valid SendEmail sendEmail) {
        return ResponseEntity.ok(feedbackService.sendMail(sendEmail));
    }
}
