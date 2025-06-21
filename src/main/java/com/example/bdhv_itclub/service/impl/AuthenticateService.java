package com.example.bdhv_itclub.service.impl;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.bdhv_itclub.dto.reponse.AuthenticateResponse;
import com.example.bdhv_itclub.dto.reponse.RegisterResponse;
import com.example.bdhv_itclub.dto.request.AuthenticateRequest;
import com.example.bdhv_itclub.dto.request.RegisterRequest;
import com.example.bdhv_itclub.dto.request.VerifyUserRequest;
import com.example.bdhv_itclub.entity.Role;
import com.example.bdhv_itclub.entity.User;
import com.example.bdhv_itclub.exception.ConflictException;
import com.example.bdhv_itclub.exception.NotFoundException;
import com.example.bdhv_itclub.exception.UnauthorizationException;
import com.example.bdhv_itclub.repository.RoleRepository;
import com.example.bdhv_itclub.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final Oauth2UserService oauth2UserService;




    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new NotFoundException("Not found role "+request.getRole()));
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .role(role)
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .build();
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        sendVerificationEmail(user);
        return RegisterResponse.toRegisterResponse(userRepository.save(user));
    }

    public AuthenticateResponse authenticate(AuthenticateRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizationException("User not found"));

        if (!user.isEnabled()) {
            throw new UnauthorizationException("Account not verified. Please verify your account.");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        String accessToken = jwtService.generateAccessToken(authentication);
        String refreshToken = jwtService.generateRefreshToken(authentication);
        log.info("User {} logged in", user.getUsername());

        return AuthenticateResponse.from(user, accessToken, refreshToken);
    }

    public AuthenticateResponse refreshJWT(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new UnauthorizationException("Missing refresh token");
        }

        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        try {
            if (!jwtService.validateRefreshToken(refreshToken)) {
                throw new UnauthorizationException("Invalid or expired refresh token");
            }

            Authentication auth = jwtService.createAuthentication(refreshToken);
            User user = userRepository.findByEmail(auth.getName())
                    .orElseThrow(() -> new UnauthorizationException("User not found with email: " + auth.getName()));

            String newAccessToken = jwtService.generateAccessToken(auth);
            log.info("User {} refreshed token", user.getUsername());

            return AuthenticateResponse.from(user, newAccessToken, refreshToken);

        } catch (JWTVerificationException e) {
            throw new UnauthorizationException("Invalid signature or token tampering detected");
        } catch (Exception e) {
            throw new UnauthorizationException("Unexpected error while refreshing token");
        }
    }



    public void verifyUser(VerifyUserRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationCode().equals(request.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }




    private void sendVerificationEmail(User user) { //TODO: Update with company logo
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
