package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.CourseResponse;
import com.example.bdhv_itclub.dto.reponse.CourseReturnHomePageResponse;
import com.example.bdhv_itclub.dto.reponse.CourseReturnSearch;
import com.example.bdhv_itclub.dto.request.CoursesRequest;
import com.example.bdhv_itclub.service.CourseService;
import com.example.bdhv_itclub.utils.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/home-page")
    public ResponseEntity<List<CourseReturnHomePageResponse>> getCourseReturnHomePage(@RequestParam(value = "categoryId", required = false) Integer categoryId) {
        List<CourseReturnHomePageResponse> listCourses = courseService.getCourseIntoHomePage(categoryId);
        if (listCourses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listCourses);
    }

    @GetMapping("/list-all")
    @ApiMessage("List all courses")
    public ResponseEntity<List<CourseResponse>> listAllCourses(@RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "categoryId", required = false) Integer categoryId) {
        List<CourseResponse> courseResponses = courseService.getAll();
        if (courseResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courseResponses);
    }

    @GetMapping("/get-detail/{slug}")
    @ApiMessage("Get the course by slug")
    public ResponseEntity<?> getCourseDetailById(@PathVariable(value = "slug") String slug) {
        return ResponseEntity.ok(courseService.getCourseDetail(slug));
    }

    @GetMapping("/search")
    @ApiMessage("Search course")
    public ResponseEntity<?> search(@RequestParam(value = "keyword") String keyword) {
        List<CourseReturnSearch> listCourses = courseService.listAllCourseByKeyword(keyword);
        if (listCourses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listCourses);
    }

    @PostMapping("/switch-enabled")
    public ResponseEntity<?> updateIsEnabled(@RequestParam(value = "course") Integer courseId, @RequestParam(value = "enabled") boolean isEnabled) {
        return ResponseEntity.ok(courseService.updateIsEnabled(courseId, isEnabled));
    }

    @PostMapping("/switch-published")
    public ResponseEntity<?> updateIsPublished(@RequestParam(value = "course") Integer courseId, @RequestParam(value = "published") boolean isPublished) {
        return ResponseEntity.ok(courseService.updateIsPublished(courseId, isPublished));
    }

    @PostMapping("/switch-finished")
    public ResponseEntity<?> updateIsFinished(@RequestParam(value = "course") Integer courseId, @RequestParam(value = "finished") boolean isFinished) {
        return ResponseEntity.ok(courseService.updateIsFinished(courseId, isFinished));
    }

    @PostMapping("/create")
    @ApiMessage("Create a course")
    public ResponseEntity<?> createCourse(@RequestPart(value = "course") @Valid CoursesRequest coursesRequest, @RequestParam(value = "img") MultipartFile img) {
        CourseResponse courseResponse = courseService.create(coursesRequest, img);
        URI uri = URI.create("/api/courses/create/" + courseResponse.getId());

        return ResponseEntity.created(uri).body(courseResponse);
    }

    @GetMapping("/get/{id}")
    @ApiMessage("Get the course by id")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable(value = "id") Integer courseId) {
        return ResponseEntity.ok(courseService.get(courseId));
    }

    @PutMapping("/update/{id}")
    @ApiMessage("Update the course")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable(value = "id") Integer courseId, @RequestPart(value = "course") @Valid CoursesRequest coursesRequest, @RequestParam(value = "img", required = false) MultipartFile img) {
        return ResponseEntity.ok(courseService.update(courseId, coursesRequest, img));
    }
}
