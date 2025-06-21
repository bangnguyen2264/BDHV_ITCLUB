package com.example.bdhv_itclub.entity;

import com.example.bdhv_itclub.constant.CourseInformationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 60, nullable = false, unique = true)
    private String title;

    @Column(length = 70, nullable = false, unique = true)
    private String slug;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(length = 100, nullable = false)
    private String thumbnail;

    @Column(nullable = false)
    private int price;

    private float discount;

    @Column(name = "student_count")
    private int studentCount;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "is_published")
    private boolean isPublished;

    @Column(name = "is_finished")
    private boolean isFinished;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CourseCategory category;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseInformation> courseInformations = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseChapter> courseChapters = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Enrollment> orders = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseTracking> courseTrackings = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certificate> certificates = new ArrayList<>();

    public Course(String title) {
        this.title = title;
    }

    public void addCourseInformations(String value, CourseInformationType courseInformationType) {
        this.courseInformations.add(new CourseInformation(value, courseInformationType, this));
    }

    public void setCourseInformations(List<CourseInformation> courseInformations) {
        if (courseInformations != null && !courseInformations.isEmpty()) {
            this.courseInformations.clear();
            this.courseInformations.addAll(courseInformations);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course courses)) return false;
        return Objects.equals(getId(), courses.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}