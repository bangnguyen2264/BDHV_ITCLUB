package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.TextLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextLessonRepository extends JpaRepository<TextLesson, Integer> {

}
