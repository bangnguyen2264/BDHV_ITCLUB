package com.example.bdhv_itclub.service;

import com.example.bdhv_itclub.dto.reponse.CourseResponse;
import com.example.bdhv_itclub.dto.reponse.CourseResponseForDetailPage;
import com.example.bdhv_itclub.dto.reponse.CourseResponseForHomePage;
import com.example.bdhv_itclub.dto.reponse.CourseResponseForSearching;
import com.example.bdhv_itclub.dto.request.CourseRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface CourseService {
    CourseResponse get(Integer courseId);
    Page<CourseResponseForHomePage> getCoursesForHomePageAndByCategoryId(Integer categoryId, int page, int size);
    CourseResponseForDetailPage getCourseForDetailPage(String slug);
    Page<CourseResponse> getAll(int page, int size);
    Page<CourseResponseForSearching> getAllByKeyword(String keyword, int page, int size);
    CourseResponse create(CourseRequest coursesRequest, MultipartFile courseThumbnail);
    CourseResponse update(Integer courseId, CourseRequest coursesRequest, MultipartFile courseThumbnail);
    String updateIsEnabled(Integer courseId, boolean isEnabled);
    String updateIsPublished(Integer courseId, boolean isPublished);
    String updateIsFinished(Integer courseId, boolean isFinished);
}