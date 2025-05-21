package com.example.bdhv_itclub.service;

import com.example.bdhv_itclub.dto.reponse.ApiResponse;
import com.example.bdhv_itclub.dto.reponse.UserResponse;
import com.example.bdhv_itclub.dto.request.UserRequest;
import com.example.bdhv_itclub.dto.request.UserFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    ApiResponse<List<UserResponse>> getAll(UserFilterRequest filter, Pageable pageable);
    UserResponse getById(Integer id);
    UserResponse update(Integer id, UserRequest request);
    void disableUser(Integer id);
}
