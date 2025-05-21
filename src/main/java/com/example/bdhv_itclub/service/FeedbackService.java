package com.example.bdhv_itclub.service;



import com.example.bdhv_itclub.dto.reponse.FeedbackResponse;
import com.example.bdhv_itclub.dto.request.FeedbackRequest;
import com.example.bdhv_itclub.dto.request.SendEmail;

import java.util.List;

public interface FeedbackService {
    FeedbackResponse save(FeedbackRequest feedbackRequest);

    FeedbackResponse get(Integer feedbackId);

    List<FeedbackResponse> listAll();

    String delete(Integer feedbackId);

    String sendMail(SendEmail sendEmail);
}
