package com.example.bdhv_itclub.repository;


import com.example.bdhv_itclub.entity.Chapter;
import com.example.bdhv_itclub.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    boolean existsLessonByNameAndChapter(String name, Chapter chapter);

    Lesson findLessonByNameAndChapter(String name, Chapter chapter);

}
