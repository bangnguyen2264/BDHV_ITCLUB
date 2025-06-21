package com.example.bdhv_itclub.entity;

import com.example.bdhv_itclub.constant.QuizType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quiz")
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
    private List<QuizAnswer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecordDetail> recordDetails = new ArrayList<>();

    public Quiz(Quiz quiz, Lesson lesson) {
        this.question = quiz.getQuestion();
        this.quizType = quiz.getQuizType();
        this.lesson = lesson;
        quiz.getAnswers().forEach(answer -> {
            addAnAnswer(answer.getContent(), answer.isCorrect());
        });
    }

    public Quiz(Quiz quiz, Contest contest) {
        this.question = quiz.getQuestion();
        this.quizType = quiz.getQuizType();
        this.contest = contest;
        quiz.getAnswers().forEach(answer -> {
            addAnAnswer(answer.getContent(), answer.isCorrect());
        });
    }

    public void addAnAnswer(String content, boolean isCorrect) {
        answers.add(new QuizAnswer(content, isCorrect, this));
    }

    public void setAnswers(List<QuizAnswer> answers) {
        if(answers != null && !answers.isEmpty()) {
            this.answers.clear();
            this.answers.addAll(answers);
        }
    }
}