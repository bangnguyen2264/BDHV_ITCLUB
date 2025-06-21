package com.example.bdhv_itclub.dto.reponse;


import com.example.bdhv_itclub.constant.CommonStatus;
import com.example.bdhv_itclub.entity.Role;
import com.example.bdhv_itclub.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    @JsonProperty("user_id")
    private Integer id;

    @JsonProperty("full_name")
    private String fullName;

    private String username;

    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String photo;

    @JsonProperty("created_time")
    private Instant createdTime;

    private boolean enabled;

    private CommonStatus status;

    private String role;

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .photo(user.getPhoto())
                .createdTime(user.getCreatedTime())
                .enabled(user.isEnabled())
                .status(user.getStatus())
                .role(user.getRole().getName())
                .build();
    }
}