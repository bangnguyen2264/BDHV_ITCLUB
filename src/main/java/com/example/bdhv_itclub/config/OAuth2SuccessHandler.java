package com.example.bdhv_itclub.config;

import com.example.bdhv_itclub.dto.reponse.AuthenticateResponse;
import com.example.bdhv_itclub.entity.Role;
import com.example.bdhv_itclub.entity.User;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.repository.RoleRepository;
import com.example.bdhv_itclub.repository.UserRepository;
import com.example.bdhv_itclub.service.impl.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = (String) oAuth2User.getAttributes().get("email");
            String fullName = (String) oAuth2User.getAttributes().getOrDefault("name", email);

            // Tìm hoặc tạo mới user
            User user = userRepository.findByEmail(email).orElseGet(() -> {
                Role roleUser = roleRepository.findByName("ROLE_USER")
                        .orElseThrow(() -> new NotFoundException("ROLE_USER not found"));
                return userRepository.save(User.builder()
                        .email(email)
                        .fullName(fullName)
                        .enabled(true)
                        .password("") // Không cần mật khẩu nếu dùng OAuth2
                        .role(roleUser)
                        .build());
            });

            // Tạo token
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
            String accessToken = jwtService.generateAccessToken(authToken);
            String refreshToken = jwtService.generateRefreshToken(authToken);

            // Trả về JSON
            AuthenticateResponse tokenResponse = AuthenticateResponse.from(user, accessToken, refreshToken);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), tokenResponse);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(), Map.of(
                    "status", 500,
                    "message", "Lỗi xác thực OAuth2",
                    "error", e.getMessage()
            ));
        }
    }
}
