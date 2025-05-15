package com.example.bdhv_itclub.service;

import com.example.bdhv_itclub.dto.reponse.UserResponse;
import com.example.bdhv_itclub.dto.request.ChangePasswordRequest;
import com.example.bdhv_itclub.dto.request.UserRequest;

public interface ResidentService {
    UserResponse getResident();
    UserResponse updateResident(UserRequest request);
    void changePassword(ChangePasswordRequest request);
}
