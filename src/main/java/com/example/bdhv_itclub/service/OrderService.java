package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.reponse.CourseOrderResponse;
import com.example.bdhv_itclub.entity.*;

import java.util.List;

public interface OrderService {
    List<CourseOrderResponse> getAll();
    List<CourseOrderResponse> getAllByUser(Integer userId);
    void createOrder(User user, Course course, int totalPrice);
}
