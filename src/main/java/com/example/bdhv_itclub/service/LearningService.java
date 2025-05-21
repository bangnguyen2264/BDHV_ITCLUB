package com.example.bdhv_itclub.service;

import com.example.bdhv_itclub.dto.reponse.CourseReturnLearningPageResponse;
import com.example.bdhv_itclub.dto.reponse.CourseReturnMyLearning;

import java.util.List;

public interface LearningService {
    List<CourseReturnMyLearning> listAllCourseRegisteredByCustomer(String email);
    boolean isRegisterInThisCourse(String slug, String email);
    CourseReturnLearningPageResponse getCourseReturnLearningPage(String slug);
}
