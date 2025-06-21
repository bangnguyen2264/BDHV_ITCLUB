package com.example.bdhv_itclub.service;



import com.example.bdhv_itclub.dto.reponse.QuestionAndAnswerResponse;
import com.example.bdhv_itclub.dto.request.QuestionAndAnswerRequest;

import java.util.List;

public interface QuestionAndAnswerService {
    List<QuestionAndAnswerResponse> listAll(Integer lessonId);
    List<QuestionAndAnswerResponse> listAllForAdmin();
    QuestionAndAnswerResponse createQuestionAndAnswer(QuestionAndAnswerRequest questionAndAnswerRequest, String email);
    QuestionAndAnswerResponse updateQuestionAndAnswer(Integer questionAndAnswerId, String content, String email);
    String deleteQuestionAndAnswer(Integer questionAndAnswerId, String email);
}