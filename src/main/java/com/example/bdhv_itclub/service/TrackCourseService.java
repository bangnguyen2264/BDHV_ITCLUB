package com.example.bdhv_itclub.service;

import com.example.bdhv_itclub.dto.reponse.InfoCourseRegistered;
import com.example.bdhv_itclub.dto.reponse.LessonReturnLearningResponse;

public interface TrackCourseService {
    InfoCourseRegistered listTrackCourse(String email, String slug);

    LessonReturnLearningResponse getLesson(Integer lessonId);

    Integer confirmLessonLearned(String email, Integer lessonIdPre);
}
