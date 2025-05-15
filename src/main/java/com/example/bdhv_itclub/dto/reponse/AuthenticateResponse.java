package com.example.bdhv_itclub.dto.reponse;

import com.example.bdhv_itclub.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticateResponse {
    private String id;
    private String accessToken;
    private String refreshToken;
    private String username;
    private String role;

    public static AuthenticateResponse from(User user, String accessToken, String refreshToken) {
        return AuthenticateResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getAuthorities().stream().findFirst().get().getAuthority())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
