package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.reponse.ApiResponse;
import com.example.bdhv_itclub.dto.reponse.UserResponse;
import com.example.bdhv_itclub.dto.request.UserRequest;
import com.example.bdhv_itclub.dto.request.UserFilterRequest;
import com.example.bdhv_itclub.entity.User;
import com.example.bdhv_itclub.repositorry.UserRepository;
import com.example.bdhv_itclub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public ApiResponse<UserResponse> getAll(UserFilterRequest filter, Pageable pageable) {
        Page<UserResponse> userPage = userRepository.findAll((root, query, cb) -> {
            var predicates = cb.conjunction();

            if (filter.getQuery() != null && !filter.getQuery().isBlank()) {
                String keyword = "%" + filter.getQuery().toLowerCase() + "%";
                predicates = cb.and(predicates, cb.or(
                        cb.like(cb.lower(root.get("fullName")), keyword),
                        cb.like(cb.lower(root.get("email")), keyword)
                ));
            }

            return predicates;
        }, pageable).map(UserResponse::fromUser);
        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(userPage.getContent())
                .page(userPage.getNumber())
                .entry(userPage.getSize())
                .total(userPage.getTotalElements())
                .build();
    }




    @Override
    public UserResponse getById(String id) {
        User user = findUserById(id);
        return UserResponse.fromUser(user);
    }

    @Override
    public UserResponse update(String id, UserRequest request) {
        User user = findUserById(id);
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        userRepository.save(user);
        return UserResponse.fromUser(user);
    }

    @Override
    public void disableUser(String id) {
        User user = findUserById(id);
        user.setEnabled(false);
        userRepository.save(user);
    }

    private User findUserById(String id) {
        return userRepository.findById(id).orElseThrow(()-> new  IllegalArgumentException("User with id "+id+" not found"));
    }
}
