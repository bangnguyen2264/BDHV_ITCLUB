package com.example.bdhv_itclub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "record_detail")
public class RecordDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Records record;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "records_answers",
        joinColumns = @JoinColumn(name = "record_detail_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "answer_id", referencedColumnName = "id")
    )
    private Set<QuizAnswer> answer;

    @Column(name = "perforated_content")
    private String perforatedContent;

    public RecordDetail(Records record, Quiz quiz, Set<QuizAnswer> answer, String perforatedContent) {
        this.record = record;
        this.quiz = quiz;
        this.answer = answer;
        this.perforatedContent = perforatedContent;
    }
}