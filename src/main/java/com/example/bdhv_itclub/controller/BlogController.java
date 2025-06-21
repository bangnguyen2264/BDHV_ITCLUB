package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.BlogResponse;
import com.example.bdhv_itclub.dto.request.BlogRequest;
import com.example.bdhv_itclub.service.BlogService;
import com.example.bdhv_itclub.utils.annotation.APIResponseMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/blog")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get-all")
    @APIResponseMessage("Liệt kê tất cả bài đăng")
    public ResponseEntity<?> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<BlogResponse> blogs = blogService.getAllBlogs(page, size);
        if (blogs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(blogs);
    }

    // Ok
    @GetMapping("/get-all/approved")
    @APIResponseMessage("Liệt kê tất cả bài đăng đã được duyệt")
    public ResponseEntity<?> listAllApproved(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<BlogResponse> blogs = blogService.getAllApprovedBlogs(page, size);
        if (blogs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(blogs);
    }

    // Ok
    @GetMapping("/get-all/user/{id}")
    @APIResponseMessage("Liệt kê tất cả bài đăng theo mã người dùng")
    public ResponseEntity<?> listAllByUser(
            @PathVariable(value = "id") Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<BlogResponse> blogs = blogService.getAllByUser(userId, page, size);
        if (blogs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(blogs);
    }

    // Ok
    @GetMapping("/get-by-slug/{slug}")
    @APIResponseMessage("Lấy bài đăng theo slug")
    public ResponseEntity<?> getBySlug(
            @PathVariable(value = "slug") String slug
    ) {
        return ResponseEntity.ok(blogService.getBySlug(slug));
    }

    // Ok
    @PutMapping("/update/view/{id}")
    @APIResponseMessage("Cập nhật lượt xem")
    public ResponseEntity<?> view(
            @PathVariable(value = "id") Integer blogId
    ) {
        return ResponseEntity.ok(blogService.view(blogId));
    }

    // Ok
    @PostMapping("/save")
    @APIResponseMessage("Tạo bài đăng mới")
    public ResponseEntity<?> save(
            @RequestPart(value = "blog") @Valid BlogRequest blogRequest,
            @RequestParam(value = "blogThumbnail") MultipartFile blogThumbnail,
            Authentication authentication
    ) {
        return new ResponseEntity<>(blogService.save(authentication.getName(), blogRequest, blogThumbnail), HttpStatus.CREATED);
    }

    // Ok
    @PutMapping("/update/{id}")
    @APIResponseMessage("Cập nhật bài đăng (trạng thái đã chuyển sang chưa duyệt)")
    public ResponseEntity<?> update(
            @PathVariable(value = "id") Integer blogId,
            @RequestPart(value = "blog") @Valid BlogRequest blogRequest,
            @RequestParam(value = "blogThumbnail", required = false) MultipartFile blogThumbnail,
            Authentication authentication
    ) {
        return ResponseEntity.ok(blogService.update(authentication.getName(), blogId, blogRequest, blogThumbnail));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/switch-approved")
    public ResponseEntity<?> switchApprovedStatusOfBlog(
            @RequestParam(value = "id") Integer blogId,
            @RequestParam(value = "approved") boolean approved
    ) {
        return ResponseEntity.ok(blogService.switchApproved(blogId, approved));
    }

    // Ok
    @DeleteMapping("/delete/{id}")
    @APIResponseMessage("Xóa bài đăng")
    public ResponseEntity<?> delete(
            @PathVariable(value = "id") Integer blogId
    ) {
        return ResponseEntity.ok(blogService.delete(blogId));
    }

    // Ok
    @GetMapping("/check-author")
    @APIResponseMessage("Kiểm tra tác giả bài đăng")
    public ResponseEntity<?> checkAuthor(
            @RequestParam(value = "userId") Integer userId, @RequestParam(value = "blogId") Integer blogId
    ) {
        return ResponseEntity.ok(blogService.checkBlogAuthor(blogId, userId));
    }

    // Ok
    @GetMapping("/search")
    @APIResponseMessage("Tìm kiếm bài đăng theo tiêu đề")
    public ResponseEntity<?> search(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<BlogResponse> blogs = blogService.search(keyword, page, size);
        if (blogs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(blogs);
    }
}