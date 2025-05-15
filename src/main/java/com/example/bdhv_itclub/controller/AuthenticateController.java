package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.AuthenticateResponse;
import com.example.bdhv_itclub.dto.reponse.RegisterResponse;
import com.example.bdhv_itclub.dto.request.AuthenticateRequest;
import com.example.bdhv_itclub.dto.request.RegisterRequest;
import com.example.bdhv_itclub.dto.request.VerifyUserRequest;
import com.example.bdhv_itclub.service.impl.AuthenticateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticateController {

    private final AuthenticateService authenticateService;
    @Operation(summary = "Register user", description = "Tạo tài khoản mới")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Đăng ký thông tin thành công"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ"),

    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticateService.register(request));
    }
    @Operation(summary = "Login", description = "Đăng nhập")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Đăng nhập thành công"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ"),
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticateResponse> authenticate(@RequestBody AuthenticateRequest request) {
        return ResponseEntity.ok(authenticateService.authenticate(request));
    }

    @PostMapping("/verify")
    @Operation(summary = "Xác thực email bằng otp", description = "Xác thực email bằng otp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xác thực thành công"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ"),
    })
    public ResponseEntity verifyUser(@RequestBody VerifyUserRequest request) {
        authenticateService.verifyUser(request);

        return ResponseEntity.ok("Verified user successfully");
    }

    @PostMapping("/resend")
    @Operation(summary = "Gửi lại otp", description = "Gửi lại otp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gửi thành công"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ"),
    })
    public ResponseEntity<String> resendVerificationCode(@RequestParam String email) {
        try {
            authenticateService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Failed to resend verification code");
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh Token", description = "Refresh Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gửi thành công"),
            @ApiResponse(responseCode = "401", description = "Token không hợp lệ"),
    })
    public ResponseEntity refreshToken(@RequestHeader("X-Refresh-Token") String refreshToken){
        return ResponseEntity.ok(authenticateService.refreshJWT(refreshToken));
    }

    @GetMapping("/oath2")
    @Operation(summary = "Login",
            description = "Load User By Google: http://localhost:8888/oauth2/authorization/google" +
            "Load User By Github: http://localhost:8888/oauth2/authorization/github ")
    public ResponseEntity<String> simulateOauth2Login(@RequestParam String email) {
        return ResponseEntity.ok("Load User By Google: http://localhost:8888/oauth2/authorization/google\n" +
                "Load User By Github: http://localhost:8888/oauth2/authorization/github ");
    }


}
