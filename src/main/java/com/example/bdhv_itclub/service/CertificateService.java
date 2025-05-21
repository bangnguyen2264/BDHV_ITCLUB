package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.reponse.CertificateResponse;
import com.example.bdhv_itclub.entity.Courses;

public interface CertificateService {
    CertificateResponse save(String email, Courses courses);
    CertificateResponse getById(Integer certificateId);
}
