package com.example.bdhv_itclub.dto.reponse;

import com.example.bdhv_itclub.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserResponse {
    private Integer id;
    private String fullName;
    private String email;
    private LocalDate dob;

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .dob(user.getDob())
                .build();
    }
}
