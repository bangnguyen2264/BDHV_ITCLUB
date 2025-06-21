package com.example.bdhv_itclub.service;



import com.example.bdhv_itclub.dto.reponse.FeedbackResponse;
import com.example.bdhv_itclub.dto.request.FeedbackEmailDTO;
import com.example.bdhv_itclub.dto.request.FeedbackRequest;

import java.util.List;

public interface FeedbackService {
    FeedbackResponse get(Integer feedbackId);
    List<FeedbackResponse> listAll();
    FeedbackResponse save(FeedbackRequest feedbackRequest);
    String delete(Integer feedbackId);
    String sendMail(FeedbackEmailDTO sendEmail);
}