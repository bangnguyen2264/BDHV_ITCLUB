package com.example.bdhv_itclub.repository;


import com.example.bdhv_itclub.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    boolean existsByCourseAndUser(Course course, User user);

    List<Enrollment> findAllByUser(User user);

    List<Enrollment> findAllByCourse(Course course);

    @Query("select e from Enrollment e where e.enrolledTime between ?1 and ?2 order by e.enrolledTime asc")
    List<Enrollment> findByEnrolledTimeBetween(Instant startTime, Instant endTime);

    @Query("select new Enrollment (e.course.category.name) from Enrollment e")
    List<Enrollment> findAllOrderCategory();

    Optional<Enrollment> findByUserAndCourse(User user, Course course);
}