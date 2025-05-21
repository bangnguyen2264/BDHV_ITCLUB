package com.example.bdhv_itclub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String question;

    @Enumerated(EnumType.STRING)
    private QuizType quizType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answerList = new ArrayList<>();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecordDetail> listRecordDetail = new ArrayList<>();

    public Quiz(Quiz quiz, Lesson lesson) {
        this.question = quiz.getQuestion();
        this.quizType = quiz.getQuizType();
        this.lesson = lesson;
//        for (Answer answer : quiz.getAnswerList()){
//            add(answer.getContent(), answer.isCorrect());
//        }
        quiz.getAnswerList().forEach(answer -> {
            add(answer.getContent(), answer.isCorrect());
        });
    }

    public Quiz(Quiz quiz, Contest contest) {
        this.question = quiz.getQuestion();
        this.quizType = quiz.getQuizType();
        this.contest = contest;
//        for (Answer answer : quiz.getAnswerList()){
//            add(answer.getContent(), answer.isCorrect());
//        }
        quiz.getAnswerList().forEach(answer -> {
            add(answer.getContent(), answer.isCorrect());
        });
    }

    public void add(String content, boolean isCorrect){
        answerList.add(new Answer(content, isCorrect, this));
    }

    public void setAnswerList(List<Answer> answerList) {
        if(answerList != null && !answerList.isEmpty()){
            this.answerList.clear();
            this.answerList.addAll(answerList);
        }
    }

}
