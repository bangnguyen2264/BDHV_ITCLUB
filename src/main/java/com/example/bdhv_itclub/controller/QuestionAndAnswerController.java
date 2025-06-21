package com.example.bdhv_itclub.controller;


import com.example.bdhv_itclub.dto.request.QuestionAndAnswerRequest;
import com.example.bdhv_itclub.service.QuestionAndAnswerService;
import com.example.bdhv_itclub.utils.annotation.APIResponseMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/question-and-answer")
public class QuestionAndAnswerController {
    private final QuestionAndAnswerService questionAndAnswerService;

    public QuestionAndAnswerController(QuestionAndAnswerService questionAndAnswerService) {
        this.questionAndAnswerService = questionAndAnswerService;
    }

    // Ok
    @GetMapping("/get-all")
    @APIResponseMessage("Liệt kê tất cả câu hỏi đáp")
    public ResponseEntity<?> listAll(
            @RequestParam(value = "lesson") Integer lessonId
    ) {
        return ResponseEntity.ok(questionAndAnswerService.listAll(lessonId));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get-all-list")
    @APIResponseMessage("Liệt kê tất cả câu hỏi đáp cho quản trị viên")
    public ResponseEntity<?> listAllForAdmin() {
        return ResponseEntity.ok(questionAndAnswerService.listAllForAdmin());
    }

    // Ok
    @PostMapping("/create")
    @APIResponseMessage("Tạo câu hỏi đáp")
    public ResponseEntity<?> create(
            @RequestBody @Valid QuestionAndAnswerRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return new ResponseEntity<>(
                questionAndAnswerService.createQuestionAndAnswer(request, email),
                HttpStatus.CREATED
        );
    }

    // Ok
    @PutMapping("/update/{id}")
    @APIResponseMessage("Sửa câu hỏi đáp")
    public ResponseEntity<?> update(
            @PathVariable(value = "id") Integer questionAndAnswerId,
            @RequestParam(value = "content") String content,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(questionAndAnswerService.updateQuestionAndAnswer(questionAndAnswerId, content, email));
    }

    // Ok
    @DeleteMapping("/delete/{id}")
    @APIResponseMessage("Xóa câu hỏi đáp")
    public ResponseEntity<?> delete(
            @PathVariable(value = "id") Integer questionAndAnswerId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(questionAndAnswerService.deleteQuestionAndAnswer(questionAndAnswerId, email));
    }
}