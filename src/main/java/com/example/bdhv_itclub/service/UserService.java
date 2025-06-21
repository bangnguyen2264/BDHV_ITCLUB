package com.example.bdhv_itclub.service;


import com.example.bdhv_itclub.dto.reponse.UserResponse;
import com.example.bdhv_itclub.dto.request.UserRequest;
import com.example.bdhv_itclub.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserResponse get(Integer userId);
    UserResponse getByEmail(String email);
    User getUserByEmail(String email);
    List<UserResponse> getAllUsers();
    UserResponse createUser(UserRequest userRequest, MultipartFile img);
    UserResponse updateUser(UserRequest userRequest, Integer userId, MultipartFile img);
    UserResponse updateUserInformation(String fullName, MultipartFile img, String email);
    void updateUserRefreshToken(String refreshToken, String email);
    String changePassword(String password, String email);
    String switchBlockStatus(Integer id);
    String delete(Integer userId);
}