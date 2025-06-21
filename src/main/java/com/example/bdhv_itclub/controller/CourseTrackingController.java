package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.request.CourseRegisteredInformation;
import com.example.bdhv_itclub.entity.Course;
import com.example.bdhv_itclub.service.CertificateService;
import com.example.bdhv_itclub.service.CourseTrackingService;
import com.example.bdhv_itclub.service.LessonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course-tracking")
public class CourseTrackingController {
    private final CourseTrackingService courseTrackingService;
    private final LessonService lessonService;
    private final CertificateService certificateService;

    public CourseTrackingController(CourseTrackingService courseTrackingService, LessonService lessonService, CertificateService certificateService) {
        this.courseTrackingService = courseTrackingService;
        this.lessonService = lessonService;
        this.certificateService = certificateService;
    }

    // Ok
    @GetMapping("/get-all")
    public ResponseEntity<CourseRegisteredInformation> getAll(
            @RequestParam(value = "slug") String slug,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(courseTrackingService.listAll(email, slug));
    }

    // Ok
    @GetMapping("/get-lesson")
    public ResponseEntity<?> learningLesson(
            @RequestParam(value = "lesson") Integer lessonId
    ) {
        return ResponseEntity.ok(courseTrackingService.getLesson(lessonId));
    }

    // Ok
    @PostMapping("/confirm-done")
    public ResponseEntity<?> doneLesson(
            @RequestParam(value = "lesson") Integer lessonId,
            Authentication authentication
    ) {
        Integer nextLessonId = courseTrackingService.confirmLearnedLesson(authentication.getName(), lessonId);
        Course course = lessonService.getCourse(lessonId);
        if (nextLessonId != -1) {
            return ResponseEntity.ok("CONTINUE");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(certificateService.save(authentication.getName(), course));
        }
    }
}