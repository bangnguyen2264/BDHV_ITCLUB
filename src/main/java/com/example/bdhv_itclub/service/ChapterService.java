package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.reponse.ChapterDTO;

public interface ChapterService {
    ChapterDTO create(Integer courseId, ChapterDTO chapterDto);

    ChapterDTO update(Integer courseId, Integer chapterId, ChapterDTO chapterDto);

    String delete(Integer chapterId);
}
