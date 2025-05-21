package com.example.bdhv_itclub.service;

import com.example.bdhv_itclub.dto.reponse.QAResponse;
import com.example.bdhv_itclub.dto.request.QARequest;

import java.util.List;

public interface QAService {
    List<QAResponse> listAll(Integer lessonId);
    List<QAResponse> listAllForAdmin();
    QAResponse createQA(QARequest qaRequest);
    QAResponse updateQA(Integer qaId, String content);
    String deleteQA(Integer qaId);
}
