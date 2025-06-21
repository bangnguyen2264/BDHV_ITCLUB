package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.QuestionAndAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionAndAnswerRepository extends JpaRepository<QuestionAndAnswer, Integer> {
    @Query("select qa from QuestionAndAnswer qa where qa.lesson.id =?1 and qa.parent.id is null")
    List<QuestionAndAnswer> findAllByLesson(Integer lessonId);
}