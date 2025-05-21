package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.reponse.UserResponse;
import com.example.bdhv_itclub.dto.request.ChangePasswordRequest;
import com.example.bdhv_itclub.dto.request.UserRequest;
import com.example.bdhv_itclub.entity.User;
import com.example.bdhv_itclub.repository.UserRepository;
import com.example.bdhv_itclub.service.ResidentService;
import com.example.bdhv_itclub.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResidentServiceImpl implements ResidentService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserResponse getResident() {
        return UserResponse.fromUser(getResidentUser());
    }

    @Override
    public UserResponse updateResident(UserRequest request) {
        User user = getResidentUser();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setDob(request.getDob());
        userRepository.save(user);
        return UserResponse.fromUser(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        User user = getResidentUser();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private User getResidentUser() {
        return userRepository.findByEmail(UserUtils.getResidentName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
