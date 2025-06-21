package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.CourseResponseForMyCoursesPage;
import com.example.bdhv_itclub.service.LearningService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/learning")
public class LearningController {
    private final LearningService learningService;

    public LearningController(LearningService learningService) {
        this.learningService = learningService;
    }

    // Ok
    @GetMapping("/course/{slug}")
    public ResponseEntity<?> getDetailInLearningPage(
            @PathVariable(value = "slug") String slug
    ) {
        return ResponseEntity.ok(learningService.getCourseForLearningPage(slug));
    }

    // Ok
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my/course/list-all")
    public ResponseEntity<?> getAllCoursesForMyLearning(
            Authentication authentication
    ) {
        String email = authentication.getName();
        List<CourseResponseForMyCoursesPage> courses = learningService.listAllCourseRegisteredByCustomer(email);
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courses);
    }

    // Ok
    @GetMapping("/check/exist-course/{slug}")
    public ResponseEntity<?> checkExistRegisteredCourse(
            @PathVariable("slug") String slug,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(learningService.isRegisterInThisCourse(slug, email));
    }
}