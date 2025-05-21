package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.CourseReturnMyLearning;
import com.example.bdhv_itclub.service.LearningService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/learning")
public class LearningController {
    private final LearningService learningService;

    public LearningController(LearningService learningService) {
        this.learningService = learningService;
    }

    @GetMapping("/courses/{slug}")
    public ResponseEntity<?> getDetailInLearningPage(@PathVariable(value = "slug") String slug) {
        return ResponseEntity.ok(learningService.getCourseReturnLearningPage(slug));
    }

    @GetMapping("/my/course/list-all")
    public ResponseEntity<?> getListAllCourseMyLearning(@RequestParam(value = "email") String email) {
        List<CourseReturnMyLearning> listCourse = learningService.listAllCourseRegisteredByCustomer(email);
        if (listCourse.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listCourse);
    }

    @GetMapping("/check/exist-course/{slug}")
    public ResponseEntity<?> checkExistRegisterCourse(@PathVariable(value = "slug") String slug, @RequestParam(value = "email") String email) {
        return ResponseEntity.ok(learningService.isRegisterInThisCourse(slug, email));
    }
}
