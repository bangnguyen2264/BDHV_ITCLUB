package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.UserResponse;
import com.example.bdhv_itclub.dto.request.ChangePasswordRequest;
import com.example.bdhv_itclub.dto.request.UserRequest;
import com.example.bdhv_itclub.service.ResidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resident")
@RequiredArgsConstructor
public class ResidentController {

    private final ResidentService residentService;

    @Operation(summary = "Lấy thông tin người dùng hiện tại", description = "Trả về thông tin của người dùng đang đăng nhập.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy thông tin thành công"),
            @ApiResponse(responseCode = "401", description = "Không có quyền truy cập")
    })
    @GetMapping
    public ResponseEntity<UserResponse> getResidentInfo() {
        return ResponseEntity.ok(residentService.getResident());
    }

    @Operation(summary = "Cập nhật thông tin người dùng", description = "Cập nhật họ tên, username, email và ngày sinh của người dùng.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thông tin thành công"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "401", description = "Không có phân quyền truy cập"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập ")
    })
    @PutMapping
    public ResponseEntity<UserResponse> updateResident(@RequestBody UserRequest request) {
        return ResponseEntity.ok(residentService.updateResident(request));
    }

    @Operation(summary = "Đổi mật khẩu", description = "Cho phép người dùng đổi mật khẩu bằng cách cung cấp mật khẩu hiện tại và mật khẩu mới.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Đổi mật khẩu thành công"),
            @ApiResponse(responseCode = "401", description = "Không có phân quyền truy cập"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập ")
    })
    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        residentService.changePassword(request);
        return ResponseEntity.ok("Changed password successfully");
    }
}
