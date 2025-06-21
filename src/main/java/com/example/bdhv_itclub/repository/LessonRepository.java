package com.example.bdhv_itclub.repository;


import com.example.bdhv_itclub.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    boolean existsByNameAndChapter(String name, CourseChapter chapter);

    Lesson findByNameAndChapter(String name, CourseChapter chapter);

    List<Lesson> findByChapterOrderByLessonOrderAsc(CourseChapter courseChapter);
}