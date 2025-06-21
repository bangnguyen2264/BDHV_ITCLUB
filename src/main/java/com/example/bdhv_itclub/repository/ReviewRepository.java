package com.example.bdhv_itclub.repository;


import com.example.bdhv_itclub.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    boolean existsReviewByUserAndCourse(User user, Course course);
    List<Review> findByCourse(Course course);
}