package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.QuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Integer> {
    @Query("select q from QuizAnswer q where q.quiz.id =?1 and q.isCorrect = true")
    List<QuizAnswer> listAllCorrectAnswer(Integer quizId);

    @Query("select q from QuizAnswer q where q.id =?1 and q.isCorrect = true")
    QuizAnswer checkCorrectAnswer(Integer quizAnswerId);
}