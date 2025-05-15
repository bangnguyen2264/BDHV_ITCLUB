package com.example.bdhv_itclub.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthenticateRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
