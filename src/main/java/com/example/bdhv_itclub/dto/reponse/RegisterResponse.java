package com.example.bdhv_itclub.dto.reponse;

import com.example.bdhv_itclub.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private String fullName;
    private String email;
    private boolean enabled;

    public static RegisterResponse toRegisterResponse(User user) {
        return RegisterResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .build();
    }
}