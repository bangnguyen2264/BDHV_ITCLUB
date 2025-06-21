package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.request.CourseCategoryDTO;
import com.example.bdhv_itclub.service.CourseCategoryService;
import com.example.bdhv_itclub.utils.annotation.APIResponseMessage;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/course-category")
public class CourseCategoryController {
    private final CourseCategoryService courseCategoryService;

    public CourseCategoryController(CourseCategoryService courseCategoryService) {
        this.courseCategoryService = courseCategoryService;
    }

    // Ok
    @GetMapping("/list-all")
    @APIResponseMessage("Liệt kê tất cả danh mục khóa học")
    public ResponseEntity<List<CourseCategoryDTO>> listAllCourseCategories(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword
    ) {
        List<CourseCategoryDTO> courseCategories = courseCategoryService.listAllCourseCategories(keyword);
        if (courseCategories.size() == 0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courseCategories);
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list-all/deleted")
    @APIResponseMessage("Liệt kê tất cả danh mục khóa học đã bị xóa")
    public ResponseEntity<List<CourseCategoryDTO>> listAllDeletedCourseCategories(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword
    ) {
        List<CourseCategoryDTO> courseCategories = courseCategoryService.listAllDeletedCourseCategories(keyword);
        if (courseCategories.size() == 0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courseCategories);
    }

    // Ok
    @GetMapping("/get/{id}")
    @APIResponseMessage("Lấy danh mục khóa học theo mã")
    public ResponseEntity<CourseCategoryDTO> get(
            @PathVariable(value = "id") Integer categoryId
    ) {
        return ResponseEntity.ok(courseCategoryService.get(categoryId));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @APIResponseMessage("Tạo danh mục khóa học mới")
    public ResponseEntity<CourseCategoryDTO> add(
            @RequestBody @Valid CourseCategoryDTO categoryRequest
    ) {
        CourseCategoryDTO savedCategory = courseCategoryService.create(categoryRequest);
        URI uri = URI.create("/api/course-category/" + savedCategory.getId());
        return ResponseEntity.created(uri).body(savedCategory);
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    @APIResponseMessage("Cập nhật danh mục khóa học")
    public ResponseEntity<CourseCategoryDTO> updatedCategory(
            @PathVariable(value = "id") Integer categoryId,
            @RequestBody @Valid CourseCategoryDTO categoryRequest
    ) {
        return ResponseEntity.ok(courseCategoryService.update(categoryId, categoryRequest));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    @APIResponseMessage("Xóa danh mục khóa học")
    public ResponseEntity<String> deleteCategory(
            @PathVariable(value = "id") Integer categoryId
    ) {
        return ResponseEntity.ok(courseCategoryService.delete(categoryId));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/restore")
    @APIResponseMessage("Khôi phục danh mục khóa học đã xóa")
    public ResponseEntity<?> restore(
            @PathVariable(value = "id") Integer categoryId
    ) {
        return ResponseEntity.ok(courseCategoryService.restore(categoryId));
    }
}