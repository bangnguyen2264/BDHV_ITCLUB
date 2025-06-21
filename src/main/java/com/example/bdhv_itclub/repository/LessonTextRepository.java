package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.LessonText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonTextRepository extends JpaRepository<LessonText, Integer> {

}
