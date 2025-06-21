package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.LessonResponse;
import com.example.bdhv_itclub.dto.request.LessonRequest;
import com.example.bdhv_itclub.dto.request.LessonTextDTO;
import com.example.bdhv_itclub.dto.request.QuizRequest;
import com.example.bdhv_itclub.dto.request.VideoDTO;
import com.example.bdhv_itclub.entity.LessonText;
import com.example.bdhv_itclub.entity.Video;
import com.example.bdhv_itclub.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/lesson")
public class LessonController {
    private final VideoService videoService;
    private final LessonTextService lessonTextService;
    private final LessonService lessonService;

    public LessonController(VideoService videoService, LessonTextService lessonTextService, LessonService lessonService) {
        this.videoService = videoService;
        this.lessonTextService = lessonTextService;
        this.lessonService = lessonService;
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get/{id}")
    public ResponseEntity<LessonResponse> get(
            @PathVariable(value = "id") Integer lessonId
    ) {
        return ResponseEntity.ok(lessonService.get(lessonId));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createLesson(@RequestPart(value = "lesson") @Valid LessonRequest lessonRequest, @RequestParam(value = "videoUpload", required = false) MultipartFile videoUpload, @RequestPart(value = "video", required = false) VideoDTO videoDTO, @RequestPart(value = "text", required = false) @Valid LessonTextDTO lessonTextDTO, @RequestPart(value = "quizzes", required = false) @Valid QuizRequest[] quizRequest) {
        Video savedVideo = null;
        LessonText savedText = null;
        switch (lessonRequest.getLessonType()) {
            case "VIDEO" -> savedVideo = videoService.save(videoDTO, videoUpload);
            case "TEXT" -> savedText = lessonTextService.create(lessonTextDTO);
        }
        return new ResponseEntity<>(lessonService.create(lessonRequest, savedVideo, savedText, quizRequest), HttpStatus.CREATED);
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<LessonResponse> update(@PathVariable(value = "id") Integer lessonId, @RequestPart(value = "lesson") @Valid LessonRequest lessonRequest, @RequestParam(value = "videoUpload", required = false) MultipartFile videoUpload, @RequestPart(value = "video", required = false) VideoDTO videoDTO, @RequestPart(value = "text", required = false) @Valid LessonTextDTO lessonTextDTO, @RequestPart(value = "quizzes", required = false) @Valid QuizRequest[] quizRequests) {
        Video savedVideo = null;
        LessonText savedText = null;
        switch (lessonRequest.getLessonType()) {
            case "VIDEO" -> savedVideo = videoService.update(videoDTO, videoUpload);
            case "TEXT" -> savedText = lessonTextService.update(lessonTextDTO);
        }
        return ResponseEntity.ok(lessonService.update(lessonId, lessonRequest, savedVideo, savedText, quizRequests));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable(value = "id") Integer lessonId) {
        return ResponseEntity.ok(lessonService.delete(lessonId));
    }
}