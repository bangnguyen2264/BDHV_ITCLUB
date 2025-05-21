package com.example.bdhv_itclub.service;

import com.example.bdhv_itclub.dto.reponse.CourseResponse;
import com.example.bdhv_itclub.dto.reponse.CourseReturnDetailPageResponse;
import com.example.bdhv_itclub.dto.reponse.CourseReturnHomePageResponse;
import com.example.bdhv_itclub.dto.reponse.CourseReturnSearch;
import com.example.bdhv_itclub.dto.request.CoursesRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseService {
    List<CourseReturnHomePageResponse> getCourseIntoHomePage(Integer categoryId);

    CourseReturnDetailPageResponse getCourseDetail(String slug);

    List<CourseReturnSearch> listAllCourseByKeyword(String keyword);

    List<CourseResponse> getAll();

    String updateIsEnabled(Integer courseId, boolean isEnabled);

    String updateIsPublished(Integer courseId, boolean isPublished);

    String updateIsFinished(Integer courseId, boolean isFinished);

    CourseResponse create(CoursesRequest coursesRequest, MultipartFile image);

    CourseResponse get(Integer courseId);

    CourseResponse update(Integer courseId, CoursesRequest coursesRequest, MultipartFile img);
}
