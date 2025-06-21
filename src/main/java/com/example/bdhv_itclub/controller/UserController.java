package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.UserResponse;
import com.example.bdhv_itclub.dto.request.UserRequest;
import com.example.bdhv_itclub.service.UserService;
import com.example.bdhv_itclub.utils.annotation.APIResponseMessage;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list-all")
    @APIResponseMessage("Liệt kê tất cả người dùng")
    public ResponseEntity<List<UserResponse>> listAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get/{id}")
    @APIResponseMessage("Lấy thông tin người dùng theo mã (admin)")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable(value = "id") Integer userId
    ) {
        return ResponseEntity.ok(userService.get(userId));
    }

    // Ok
    @GetMapping("/me")
    @APIResponseMessage("Lấy thông tin người dùng đang đăng nhập")
    public ResponseEntity<UserResponse> getCurrentUser(
            Authentication authentication
    ) {
        return ResponseEntity.ok(userService.getByEmail(authentication.getName()));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @APIResponseMessage("Thêm người dùng")
    public ResponseEntity<UserResponse> createUser(
            @RequestPart(value = "user") @Valid UserRequest userRequest,
            @RequestParam(value = "img", required = false) MultipartFile image
    ) {
        UserResponse savedUser = userService.createUser(userRequest, image);
        URI uri = URI.create("/api/user/" + savedUser.getId());
        return ResponseEntity.created(uri).body(savedUser);
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    @APIResponseMessage("Cập nhật người dùng")
    public ResponseEntity<UserResponse> updateUser(
            @RequestPart(value = "user") @Valid UserRequest userRequest,
            @PathVariable(value = "id") Integer userId,
            @RequestParam(value = "img", required = false) MultipartFile img
    ) {
        return ResponseEntity.ok(userService.updateUser(userRequest, userId, img));
    }

    // Ok
    @PostMapping("/change-info")
    @APIResponseMessage("Thay đổi thông tin người dùng")
    public ResponseEntity<UserResponse> updateUserInformation(
            @RequestParam(value = "full_name", required = false) String fullName,
            @RequestParam(value = "img", required = false) MultipartFile img,
            Authentication authentication
    ) {
        return ResponseEntity.ok(userService.updateUserInformation(fullName, img, authentication.getName()));
    }

    // Ok
    @PostMapping("/change-password")
    @APIResponseMessage("Thay đổi mật khẩu")
    public ResponseEntity<String> changePassword(
            @RequestParam(value = "password") String newPassword,
            Authentication authentication
    ) {
        return ResponseEntity.ok(userService.changePassword(newPassword, authentication.getName()));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/switch-blocked/{id}")
    @APIResponseMessage("Thay đổi trạng thái người dùng (block)")
    public ResponseEntity<String> switchBlockStatus(
            @PathVariable(value = "id") Integer id
    ) {
        return ResponseEntity.ok(userService.switchBlockStatus(id));
    }

    // Ok
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    @APIResponseMessage("Xóa người dùng")
    public ResponseEntity<String> delete(
            @PathVariable(value = "id") Integer userId
    ) {
        return ResponseEntity.ok(userService.delete(userId));
    }
}