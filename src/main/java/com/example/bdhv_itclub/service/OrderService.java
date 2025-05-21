package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.reponse.OrderResponse;
import com.example.bdhv_itclub.entity.Courses;
import com.example.bdhv_itclub.entity.User;

import java.util.List;

public interface OrderService {
    List<com.example.bdhv_itclub.dto.reponse.OrderResponse> getAllByUser(Integer userId);

    void createOrder(User user, Courses courses, int totalPrice);

    List<OrderResponse> getAll();
}
