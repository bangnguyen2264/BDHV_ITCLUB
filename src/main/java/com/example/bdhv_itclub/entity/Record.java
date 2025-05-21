package com.example.bdhv_itclub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "records")
public class Record {

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

    @Column(nullable = false, name = "total_answer_correct")
    private float totalAnswerCorrect;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecordDetail> listRecordDetails = new ArrayList<>();

    public void add(Quiz quiz, Set<Answer> answer, String contentPerforate){
        this.listRecordDetails.add(new RecordDetail(this, quiz, answer, contentPerforate));
    }

    public Record(String title, float grade){
        this.contest = new Contest(title);
        this.grade = grade;
    }
}
