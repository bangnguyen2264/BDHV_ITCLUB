package com.example.bdhv_itclub.controller;


import com.example.bdhv_itclub.dto.request.FeedbackEmailDTO;
import com.example.bdhv_itclub.dto.request.FeedbackRequest;
import com.example.bdhv_itclub.service.FeedbackService;
import com.example.bdhv_itclub.utils.annotation.APIResponseMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // Ok
    @PostMapping("/send")
    public ResponseEntity<?> sendFeedback(
            @RequestBody @Valid FeedbackRequest feedbackRequest
    ) {
        return new ResponseEntity<>(feedbackService.save(feedbackRequest), HttpStatus.CREATED);
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get/{id}")
    @APIResponseMessage("Lấy nhận xét theo mã")
    public ResponseEntity<?> get(
            @PathVariable(value = "id") Integer feedbackId
    ) {
        return ResponseEntity.ok(feedbackService.get(feedbackId));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list-all")
    @APIResponseMessage("Liệt kê tất cả nhận xét")
    public ResponseEntity<?> listAll() {
        return ResponseEntity.ok(feedbackService.listAll());
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    @APIResponseMessage("Xóa nhận xét")
    public ResponseEntity<?> delete(
            @PathVariable(value = "id") Integer feedbackId
    ) {
        return ResponseEntity.ok(feedbackService.delete(feedbackId));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/send-email")
    public ResponseEntity<?> sendEmail(
            @RequestBody @Valid FeedbackEmailDTO sendEmail
    ) {
        return ResponseEntity.ok(feedbackService.sendMail(sendEmail));
    }
}