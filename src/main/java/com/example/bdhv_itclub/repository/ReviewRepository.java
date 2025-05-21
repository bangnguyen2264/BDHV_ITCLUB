package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.Courses;
import com.example.bdhv_itclub.entity.Review;
import com.example.bdhv_itclub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByCourses(Courses courses);
    boolean existsReviewByUserAndCourses(User user, Courses courses);
}
