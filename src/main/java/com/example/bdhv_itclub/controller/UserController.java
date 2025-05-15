package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.ApiResponse;
import com.example.bdhv_itclub.dto.reponse.UserResponse;
import com.example.bdhv_itclub.dto.request.UserRequest;
import com.example.bdhv_itclub.dto.request.UserFilterRequest;
import com.example.bdhv_itclub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> getAllUsers(
            @ParameterObject() UserFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int entry
    ) {
        Sort sort = Sort.unsorted();
        if (filter.getField() != null && filter.getSort() != null) {
            Sort.Direction direction = Sort.Direction.valueOf(filter.getSort().name());
            sort = Sort.by(direction, filter.getField());
        }

        Pageable pageable = PageRequest.of(page, entry, sort);
        return ResponseEntity.ok(userService.getAll(filter, pageable));
    }

    // Lấy thông tin người dùng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    // Cập nhật thông tin người dùng
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String id,
            @RequestBody UserRequest request
    ) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    // Vô hiệu hóa người dùng
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> disableUser(@PathVariable String id) {
        userService.disableUser(id);
        return ResponseEntity.noContent().build();
    }
}
