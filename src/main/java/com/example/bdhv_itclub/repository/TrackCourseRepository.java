package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackCourseRepository extends JpaRepository<TrackCourse, Integer> {
    List<TrackCourse> findAllByCoursesAndUser(Courses courses, User user);

    List<TrackCourse> findTrackCourseByCoursesAndChapterAndUser(Courses courses, Chapter chapter, User user);

    TrackCourse findByLessonAndUser(Lesson lesson, User user);

    @Modifying
    @Query("update TrackCourse tc set tc.isCompleted = true, tc.isCurrent = false where (tc.user.id = ?1 and tc.lesson.id = ?2)")
    void updateTrackCourseLessonPre(Integer userId, Integer lessonIdPre);

    @Modifying
    @Query("update TrackCourse tc set tc.isUnlock = true, tc.isCurrent = true where (tc.user.id = ?1 and tc.lesson.id = ?2)")
    void updateTrackCourseLessonNext(Integer userId, Integer lessonIdNext);

    TrackCourse findTrackCourseByLessonAndUser(Lesson lesson, User user);

    @Query("select tc from TrackCourse tc where tc.courses.id = ?1 and tc.user.id = ?2 and tc.isCurrent = true")
    TrackCourse findTrackCoursesByCurrent(Integer courseId, Integer userId);
}
