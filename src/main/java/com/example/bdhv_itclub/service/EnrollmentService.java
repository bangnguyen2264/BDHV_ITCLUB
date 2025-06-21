package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.reponse.EnrollmentResponse;

import java.util.List;

public interface EnrollmentService {
    List<EnrollmentResponse> getAll();
    List<EnrollmentResponse> getAllByUser(String email);

    EnrollmentResponse createEnrollment(Integer courseId, String email);
}