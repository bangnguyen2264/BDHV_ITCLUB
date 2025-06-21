package com.example.bdhv_itclub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "record")
public class Records {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @Column(nullable = false, name = "joined_at")
    private Instant joinedAt;

    private float grade;

    private int period;

    @Column(nullable = false, name = "total_correct_answer")
    private float totalCorrectAnswer;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecordDetail> recordDetails = new ArrayList<>();

    public void addARecordDetail(Quiz quiz, Set<QuizAnswer> answer, String perforatedContent) {
        this.recordDetails.add(new RecordDetail(this, quiz, answer, perforatedContent));
    }

    public Records(String title, float grade) {
        this.contest = new Contest(title);
        this.grade = grade;
    }
}