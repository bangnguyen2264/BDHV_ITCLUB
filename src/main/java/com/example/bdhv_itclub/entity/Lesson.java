package com.example.bdhv_itclub.entity;

import com.example.bdhv_itclub.constant.LessonType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    private LessonType lessonType;

    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private CourseChapter chapter;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "video_id")
    private Video video;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "text_id")
    private LessonText text;

    private int lessonOrder;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes = new ArrayList<>();

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseTracking> courseTrackings = new ArrayList<>();

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionAndAnswer> questionAndAnswers = new ArrayList<>();

    public void addAQuiz(Quiz quiz) {
        this.quizzes.add(new Quiz(quiz, this));
    }

    public void setQuizzes(List<Quiz> quizzes) {
        if (quizzes != null && !quizzes.isEmpty()) {
            this.quizzes.clear();
            this.quizzes.addAll(quizzes);
        }
    }
}