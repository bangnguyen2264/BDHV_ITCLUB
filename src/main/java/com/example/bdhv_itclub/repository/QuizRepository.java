package com.example.bdhv_itclub.repository;


import com.example.bdhv_itclub.entity.Lesson;
import com.example.bdhv_itclub.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    Quiz findQuizByQuestionAndLesson(String question, Lesson lesson);

}
