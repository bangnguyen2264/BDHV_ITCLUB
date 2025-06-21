package com.example.bdhv_itclub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contest")
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 150, nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private int period;

    private boolean enabled;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes = new ArrayList<>();

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Records> records = new ArrayList<>();

    public Contest(String title) {
        this.title = title;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        if (quizzes != null && !quizzes.isEmpty()) {
            this.quizzes.clear();
            this.quizzes.addAll(quizzes);
        }
    }

    public void addAQuiz(Quiz quiz) {
        this.quizzes.add(new Quiz(quiz, this));
    }
}