package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.EnrollmentResponse;
import com.example.bdhv_itclub.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {
    private final EnrollmentService orderService;

    public EnrollmentController(EnrollmentService orderService) {
        this.orderService = orderService;
    }

    // Ok
    @GetMapping("/get-all/user")
    public ResponseEntity<?> getAllByUser(
            Authentication authentication
    ) {
        List<EnrollmentResponse> orderResponses = orderService.getAllByUser(authentication.getName());
        if (orderResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orderResponses);
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN")
    @GetMapping("/list-all")
    public ResponseEntity<?> listAll() {
        List<EnrollmentResponse> orderResponses = orderService.getAll();
        if (orderResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orderResponses);
    }

    @PostMapping("/enroll")
    public ResponseEntity<?> enroll(
            @RequestParam(value = "courseId") Integer courseId,
            Authentication authentication
    ) {
        EnrollmentResponse orderResponse = orderService.createEnrollment(courseId, authentication.getName());
        return ResponseEntity.ok(orderResponse);
    }
}