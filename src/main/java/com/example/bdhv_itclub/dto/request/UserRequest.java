package com.example.bdhv_itclub.dto.request;

import lombok.Data;

import java.time.LocalDate;
@Data
public class UserRequest {
    private String fullName;
    private String email;
    private LocalDate dob;
}
