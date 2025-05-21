package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.Chapter;
import com.example.bdhv_itclub.entity.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {
    Chapter findChapterByNameAndCourse(String name, Courses course);
}
