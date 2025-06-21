package com.example.bdhv_itclub.entity;

import com.example.bdhv_itclub.constant.CourseInformationType;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course_information")
public class CourseInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String value;

    @Enumerated(EnumType.STRING)
    private CourseInformationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    public CourseInformation(String value, CourseInformationType type, Course course) {
        this.value = value;
        this.type = type;
        this.course = course;
    }
}