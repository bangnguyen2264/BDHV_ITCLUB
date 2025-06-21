package com.example.bdhv_itclub.controller;


import com.example.bdhv_itclub.dto.request.CourseChapterDTO;
import com.example.bdhv_itclub.service.CourseChapterService;
import com.example.bdhv_itclub.utils.annotation.APIResponseMessage;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/course-chapter")
public class CourseChapterController {
    private final CourseChapterService courseChapterService;

    public CourseChapterController(CourseChapterService courseChapterService) {
        this.courseChapterService = courseChapterService;
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create/{courseId}")
    @APIResponseMessage("Thêm chương học mới")
    public ResponseEntity<CourseChapterDTO> addChapter(
            @RequestBody @Valid CourseChapterDTO courseChapterDTO,
            @PathVariable(value = "courseId") Integer courseId
    ) {
        CourseChapterDTO response = courseChapterService.create(courseId, courseChapterDTO);
        URI uri = URI.create("/api/course-chapter/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{chapterId}/{courseId}")
    @APIResponseMessage("Cập nhật chương học")
    public ResponseEntity<CourseChapterDTO> updateChapter(@PathVariable(value = "courseId") Integer courseId, @PathVariable(value = "chapterId") Integer chapterId, @RequestBody @Valid CourseChapterDTO courseChapterDTO) {
        return ResponseEntity.ok(courseChapterService.update(courseId, chapterId, courseChapterDTO));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{chapterId}")
    @APIResponseMessage("Xóa chương học")
    public ResponseEntity<String> deleteChapter(@PathVariable(value = "chapterId") Integer chapterId) {
        return ResponseEntity.ok(courseChapterService.delete(chapterId));
    }
}