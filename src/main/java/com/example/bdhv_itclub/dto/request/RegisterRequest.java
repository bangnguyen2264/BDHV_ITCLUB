package com.example.bdhv_itclub.dto.request;

import com.example.bdhv_itclub.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    @NotBlank
    private String fullName;
    @NotBlank
    @Email
    private String email;
    @NotNull
    private String role;
    @NotBlank
    @Size(min = 8)
    private String password;
}
