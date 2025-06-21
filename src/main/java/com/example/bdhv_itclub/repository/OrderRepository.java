package com.example.bdhv_itclub.repository;


import com.example.bdhv_itclub.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<CourseOrder, Integer> {
    boolean existsOrderByCourseAndUser(Course course, User user);

    List<CourseOrder> findAllByUser(User user);

    List<CourseOrder> findAllByCourse(Course course);

    @Query("select o from CourseOrder o where o.createdTime between ?1 and ?2 order by o.createdTime asc")
    List<CourseOrder> findByOrderTimeBetween(Instant startTime, Instant endTime);

    @Query("select new CourseOrder (o.course.category.name, o.totalPrice) from CourseOrder o")
    List<CourseOrder> findAllOrderCategory();

    @Query("select sum(o.totalPrice) from CourseOrder o")
    int sumInCome();
}