package com.example.bdhv_itclub.service;



import com.example.bdhv_itclub.dto.reponse.CourseResponseForLearningPage;
import com.example.bdhv_itclub.dto.reponse.CourseResponseForMyCoursesPage;

import java.util.List;

public interface LearningService {
    List<CourseResponseForMyCoursesPage> listAllCourseRegisteredByCustomer(String email);
    boolean isRegisterInThisCourse(String slug, String email);
    CourseResponseForLearningPage getCourseForLearningPage(String slug);
}