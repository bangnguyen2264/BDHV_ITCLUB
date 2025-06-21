package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.reponse.LessonResponse;
import com.example.bdhv_itclub.dto.request.LessonRequest;
import com.example.bdhv_itclub.dto.request.QuizRequest;
import com.example.bdhv_itclub.entity.*;

public interface LessonService {
    LessonResponse get(Integer lessonId);
    Course getCourse(Integer lessonId);
    LessonResponse create(LessonRequest lessonRequest, Video video, LessonText lessonText, QuizRequest[] quizRequest);
    LessonResponse update(Integer lessonId, LessonRequest lessonRequest, Video video, LessonText lessonText, QuizRequest[] quizRequest);
    String delete(Integer lessonId);
}