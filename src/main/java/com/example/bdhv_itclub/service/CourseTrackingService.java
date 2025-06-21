package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.reponse.LessonResponseForLearningPage;
import com.example.bdhv_itclub.dto.request.CourseRegisteredInformation;

public interface CourseTrackingService {
    CourseRegisteredInformation listAll(String email, String slug);
    LessonResponseForLearningPage getLesson(Integer lessonId);
    Integer confirmLearnedLesson(String email, Integer previousLessonId);
}