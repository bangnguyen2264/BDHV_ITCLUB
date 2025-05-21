package com.example.bdhv_itclub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contests")
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
    private List<Quiz> listQuizzes = new ArrayList<>();

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> listRecords = new ArrayList<>();

    public void add(Quiz quiz){
        this.listQuizzes.add(new Quiz(quiz, this));
    }

    public void setQuizList(List<Quiz> quizList) {
        if(quizList != null && !quizList.isEmpty()){
            this.listQuizzes.clear();
            this.listQuizzes.addAll(quizList);
        }
    }

    public Contest(String title) {
        this.title = title;
    }
}
