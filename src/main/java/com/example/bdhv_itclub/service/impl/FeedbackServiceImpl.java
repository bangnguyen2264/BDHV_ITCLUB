package com.example.bdhv_itclub.service.impl;


import com.example.bdhv_itclub.constant.FeedbackStatus;
import com.example.bdhv_itclub.dto.reponse.FeedbackResponse;
import com.example.bdhv_itclub.dto.request.FeedbackEmailDTO;
import com.example.bdhv_itclub.dto.request.FeedbackRequest;
import com.example.bdhv_itclub.entity.Feedback;
import com.example.bdhv_itclub.exception.BadRequestException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.FeedbackRepository;
import com.example.bdhv_itclub.service.FeedbackService;
import com.example.bdhv_itclub.utils.EmailUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final ModelMapper modelMapper;
    private final FeedbackRepository feedbackRepository;
    private final EmailUtil emailUtil;

    public FeedbackServiceImpl(ModelMapper modelMapper, FeedbackRepository feedbackRepository, EmailUtil emailUtil) {
        this.modelMapper = modelMapper;
        this.feedbackRepository = feedbackRepository;
        this.emailUtil = emailUtil;
    }

    // Ok
    @Override
    public FeedbackResponse get(Integer feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow(() -> new NotFoundException("Mã nhận xét không tồn tại"));
        return modelMapper.map(feedback, FeedbackResponse.class);
    }

    // Ok
    @Override
    public List<FeedbackResponse> listAll() {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        return feedbacks.stream().map(feedback -> modelMapper.map(feedback, FeedbackResponse.class)).toList();
    }

    // Ok
    @Override
    public FeedbackResponse save(FeedbackRequest feedbackRequest) {
        Feedback feedback = modelMapper.map(feedbackRequest, Feedback.class);
        feedback.setStatus(FeedbackStatus.NEW);
        Feedback savedFeedback = feedbackRepository.save(feedback);
        return modelMapper.map(savedFeedback, FeedbackResponse.class);
    }

    // Ok
    @Override
    public String delete(Integer feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow(() -> new NotFoundException("Mã nhận xét không tồn tại"));
        feedbackRepository.delete(feedback);
        return "Xoá nhận xét thành công";
    }

    // Ok
    @Override
    public String sendMail(FeedbackEmailDTO sendEmail) {
        Feedback feedback = feedbackRepository.findById(sendEmail.getFeedbackId())
                .orElseThrow(() -> new NotFoundException("Mã nhận xét không tồn tại"));

        if (feedback.getStatus() == FeedbackStatus.SENT) {
            throw new BadRequestException("Nhận xét này đã được phản hồi qua email");
        }
        emailUtil.sendFeedbackEmail(sendEmail);
        feedback.setStatus(FeedbackStatus.SENT);
        feedbackRepository.save(feedback);
        return "Đã phản hồi nhận xét thông qua email";
    }
}