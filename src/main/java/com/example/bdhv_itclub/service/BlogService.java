package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.reponse.BlogResponse;
import com.example.bdhv_itclub.dto.request.BlogRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface BlogService {
    String view(Integer blogId);
    Page<BlogResponse> getAllBlogs(int page, int size);
    Page<BlogResponse> getAllApprovedBlogs(int page, int size);
    Page<BlogResponse> getAllByUser(Integer userId, int page, int size);
    BlogResponse getBySlug(String slug);
    String checkBlogAuthor(Integer blogId, Integer userId);
    BlogResponse save(String email, BlogRequest blogRequest, MultipartFile blogThumbnail);
    BlogResponse update(String email, Integer blogId, BlogRequest blogRequest, MultipartFile blogThumbnail);
    String switchApproved(Integer blogId, boolean isApproved);
    String delete(Integer blogId);
    Page<BlogResponse> search(String keyword, int page, int size);
}