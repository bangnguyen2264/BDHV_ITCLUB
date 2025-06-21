package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.reponse.CertificateResponse;
import com.example.bdhv_itclub.entity.Course;

public interface CertificateService {
    CertificateResponse save(String email, Course course);
    CertificateResponse getById(Integer certificateId);
}