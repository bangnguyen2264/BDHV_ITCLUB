package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseTrackingRepository extends JpaRepository<CourseTracking, Integer> {
    List<CourseTracking> findAllByCourseAndUser(Course course, User user);

    List<CourseTracking> findByCourseAndChapterAndUser(Course course, CourseChapter chapter, User user);

    CourseTracking findByLessonAndUser(Lesson lesson, User user);

    CourseTracking findCourseTrackingByLessonAndUser(Lesson lesson, User user);

    @Query("select ct from CourseTracking ct where ct.course.id = ?1 and ct.user.id = ?2 and ct.isCurrent = true")
    CourseTracking findByCurrent(Integer courseId, Integer userId);

    @Modifying
    @Query("update CourseTracking ct set ct.isCompleted = true, ct.isCurrent = false where (ct.user.id = ?1 and ct.lesson.id = ?2)")
    void updatePreviousCourseTrackingLesson(Integer userId, Integer lessonIdPre);

    @Modifying
    @Query("update CourseTracking ct set ct.isUnlock = true, ct.isCurrent = true where (ct.user.id = ?1 and ct.lesson.id = ?2)")
    void updateNextCourseTrackingLesson(Integer userId, Integer lessonIdNext);
}