package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.request.LessonRequestInQuiz;
import com.example.bdhv_itclub.service.QuizService;
import com.example.bdhv_itclub.utils.annotation.APIResponseMessage;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // Ok
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/check-answer")
    @APIResponseMessage("Kiểm tra câu trả lời")
    public ResponseEntity<?> checkCorrectAnswer(@RequestBody @Valid LessonRequestInQuiz lessonRequestInQuiz) {
        return ResponseEntity.ok(quizService.gradeOfQuiz(lessonRequestInQuiz));
    }
}