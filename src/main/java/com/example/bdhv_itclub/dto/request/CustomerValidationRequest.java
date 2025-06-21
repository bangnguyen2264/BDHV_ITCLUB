package com.example.bdhv_itclub.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerValidationRequest {
    private String email;
    private String username;
    private String phoneNumber;
}