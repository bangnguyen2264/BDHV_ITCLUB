package com.example.bdhv_itclub.service;

import com.example.bdhv_itclub.dto.reponse.TextLessonDTO;
import com.example.bdhv_itclub.entity.TextLesson;

public interface TextLessonService {
    TextLesson create(TextLessonDTO textLessonDto);

    TextLesson update(TextLessonDTO textLessonDto);
}
