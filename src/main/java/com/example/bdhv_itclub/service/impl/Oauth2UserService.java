package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.repository.RoleRepository;
import com.example.bdhv_itclub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class Oauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(request);
        String registrationId = request.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes = user.getAttributes();
        String email = null;

        if ("google".equals(registrationId)) {
            email = (String) attributes.get("email");
        } else if ("github".equals(registrationId)) {
            email = fetchEmailFromGithub(request);
        }

        if (email == null) {
            throw new OAuth2AuthenticationException("Không lấy được email từ " + registrationId);
        }

        // Đặt lại attributes cho OAuth2User nếu cần
        Map<String, Object> newAttributes = new HashMap<>(attributes);
        newAttributes.put("email", email);

        return new DefaultOAuth2User(
                user.getAuthorities(),
                newAttributes,
                "email"
        );
    }

    private String fetchEmailFromGithub(OAuth2UserRequest request) {
        try {
            var restTemplate = new RestTemplate();
            var headers = new HttpHeaders();
            headers.setBearerAuth(request.getAccessToken().getTokenValue());
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            var entity = new HttpEntity<String>(headers);

            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    "https://api.github.com/user/emails",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );

            for (Map<String, Object> emailData : response.getBody()) {
                if (Boolean.TRUE.equals(emailData.get("primary")) && Boolean.TRUE.equals(emailData.get("verified"))) {
                    return (String) emailData.get("email");
                }
            }

        } catch (Exception e) {
            throw new OAuth2AuthenticationException("Error when call Github API "+ e);
        }
        return null;
    }

}

