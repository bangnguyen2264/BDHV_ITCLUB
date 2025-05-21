package com.example.bdhv_itclub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_time", nullable = false)
    private Instant createdTime;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Courses courses;

    // Mục đích để làm tính năng thống kê
    public Order(String categoryName, int total){
        this.courses = new Courses();
        this.courses.setCategory(new Category(categoryName));
        this.totalPrice = total;
    }
}
