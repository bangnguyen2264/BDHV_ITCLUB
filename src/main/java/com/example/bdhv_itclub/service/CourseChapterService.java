package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.request.CourseChapterDTO;

public interface CourseChapterService {
    CourseChapterDTO create(Integer courseId, CourseChapterDTO courseChapterDTO);
    CourseChapterDTO update(Integer courseId, Integer chapterId, CourseChapterDTO courseChapterDTO);
    String delete(Integer chapterId);
}