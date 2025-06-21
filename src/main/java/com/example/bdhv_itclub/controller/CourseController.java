package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.CourseResponse;
import com.example.bdhv_itclub.dto.reponse.CourseResponseForHomePage;
import com.example.bdhv_itclub.dto.reponse.CourseResponseForSearching;
import com.example.bdhv_itclub.dto.request.CourseRequest;
import com.example.bdhv_itclub.service.CourseService;
import com.example.bdhv_itclub.utils.annotation.APIResponseMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/api/course")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Ok
    @GetMapping("/home-page")
    public ResponseEntity<Page<CourseResponseForHomePage>> getCoursesForHomePageAndByCategoryId(
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<CourseResponseForHomePage> courses = courseService.getCoursesForHomePageAndByCategoryId(categoryId, page, size);
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courses);
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list-all")
    @APIResponseMessage("Liệt kê tất cả khóa học")
    public ResponseEntity<Page<CourseResponse>> listAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<CourseResponse> courseResponses = courseService.getAll(page, size);
        if (courseResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courseResponses);
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get/{id}")
    @APIResponseMessage("Lấy khóa học theo mã khóa học")
    public ResponseEntity<CourseResponse> getCourseById(
            @PathVariable(value = "id") Integer courseId
    ) {
        return ResponseEntity.ok(courseService.get(courseId));
    }

    // Ok
    @GetMapping("/get-detail/{slug}")
    @APIResponseMessage("Lấy khóa học theo slug")
    public ResponseEntity<?> getCourseForDetailPageById(
            @PathVariable(value = "slug") String slug
    ) {
        return ResponseEntity.ok(courseService.getCourseForDetailPage(slug));
    }

    // Ok
    @GetMapping("/search")
    @APIResponseMessage("Tìm kiếm khóa học")
    public ResponseEntity<?> search(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<CourseResponseForSearching> courseResponseForSearchings = courseService.getAllByKeyword(keyword, page, size);
        if (courseResponseForSearchings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courseResponseForSearchings);
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @APIResponseMessage("Tạo khóa học mới")
    public ResponseEntity<?> createCourse(
            @RequestPart(value = "course") @Valid CourseRequest coursesRequest,
            @RequestParam(value = "courseThumbnail") MultipartFile courseThumbnail
    ) {
        CourseResponse courseResponse = courseService.create(coursesRequest, courseThumbnail);
        URI uri = URI.create("/api/course/create/" + courseResponse.getId());
        return ResponseEntity.created(uri).body(courseResponse);
    }

    // Ok
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @APIResponseMessage("Cập nhật khóa học")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable(value = "id") Integer courseId,
            @RequestPart(value = "course") @Valid CourseRequest coursesRequest,
            @RequestParam(value = "courseThumbnail", required = false) MultipartFile courseThumbnail
    ) {
        return ResponseEntity.ok(courseService.update(courseId, coursesRequest, courseThumbnail));
    }

    // Ok
    @PostMapping("/switch-enabled")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateIsEnabled(
            @RequestParam(value = "course") Integer courseId,
            @RequestParam(value = "enabled") boolean isEnabled
    ) {
        return ResponseEntity.ok(courseService.updateIsEnabled(courseId, isEnabled));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/switch-published")
    public ResponseEntity<?> updateIsPublished(
            @RequestParam(value = "course") Integer courseId,
            @RequestParam(value = "published") boolean isPublished
    ) {
        return ResponseEntity.ok(courseService.updateIsPublished(courseId, isPublished));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/switch-finished")
    public ResponseEntity<?> updateIsFinished(
            @RequestParam(value = "course") Integer courseId,
            @RequestParam(value = "finished") boolean isFinished
    ) {
        return ResponseEntity.ok(courseService.updateIsFinished(courseId, isFinished));
    }
}