package com.example.bdhv_itclub.service;

import com.example.bdhv_itclub.dto.reponse.ApiResponse;
import com.example.bdhv_itclub.dto.reponse.UserResponse;
import com.example.bdhv_itclub.dto.request.UserRequest;
import com.example.bdhv_itclub.dto.request.UserFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    ApiResponse<UserResponse> getAll(UserFilterRequest filter, Pageable pageable);
    UserResponse getById(String id);
    UserResponse update(String id, UserRequest request);
    void disableUser(String id);
}
