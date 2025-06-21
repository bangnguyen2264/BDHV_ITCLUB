package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.Course;
import com.example.bdhv_itclub.entity.CourseChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseChapterRepository extends JpaRepository<CourseChapter, Integer> {
    CourseChapter findByNameAndCourse(String name, Course course);

    CourseChapter findByChapterOrderAndCourse(Integer chapterOrder, Course course);
}