package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.request.LessonTextDTO;
import com.example.bdhv_itclub.entity.LessonText;

public interface LessonTextService {
    LessonText create(LessonTextDTO textLessonDto);

    LessonText update(LessonTextDTO textLessonDto);
}
