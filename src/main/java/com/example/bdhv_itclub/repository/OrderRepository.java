package com.example.bdhv_itclub.repository;


import com.example.bdhv_itclub.entity.Courses;
import com.example.bdhv_itclub.entity.Order;
import com.example.bdhv_itclub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUser(User user);

    boolean existsOrderByCoursesAndUser(Courses courses, User user);

    List<Order> findAllByCourses(Courses courses);

    @Query("select sum(o.totalPrice) from Order o")
    int sumInCome();

    @Query("select o from Order o where o.createdTime between ?1 and ?2 order by o.createdTime asc")
    List<Order> findByOrderTimeBetween(Instant startTime, Instant endTime);

    @Query("select new Order (o.courses.category.name, o.totalPrice)from Order o")
    List<Order> findAllOrderCategory();
}
