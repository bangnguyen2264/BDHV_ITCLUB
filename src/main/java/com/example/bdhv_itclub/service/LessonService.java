package com.example.bdhv_itclub.service;

import com.example.bdhv_itclub.dto.reponse.LessonResponse;
import com.example.bdhv_itclub.dto.request.LessonRequest;
import com.example.bdhv_itclub.dto.request.QuizRequest;
import com.example.bdhv_itclub.entity.Courses;
import com.example.bdhv_itclub.entity.TextLesson;
import com.example.bdhv_itclub.entity.Video;

public interface LessonService {
    Courses getCourse(Integer lessonId);

    LessonResponse create(LessonRequest lessonRequest, Video video, TextLesson textLesson, QuizRequest[] quizRequest);

    LessonResponse get(Integer lessonId);

    LessonResponse update(Integer lessonId, LessonRequest lessonRequest, Video video, TextLesson textLesson, QuizRequest[] quizRequest);

    String delete(Integer lessonId);
}
