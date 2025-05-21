package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerQuizRepository extends JpaRepository<Answer, Integer> {
    @Query("select a from Answer a where a.id =?1 and a.isCorrect = true")
    Answer checkAnswerInCorrect(Integer answerId);

    @Query("select a from Answer a where a.quiz.id =?1 and a.isCorrect = true")
    List<Answer> listAllAnswerIsCorrect(Integer quizId);
}
