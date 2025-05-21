package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.QA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QARepository extends JpaRepository<QA, Integer> {
    @Query("select qa from QA qa where qa.lesson.id =?1 and qa.parent.id is null")
    List<QA> findAllByLesson(Integer lessonId);
}
