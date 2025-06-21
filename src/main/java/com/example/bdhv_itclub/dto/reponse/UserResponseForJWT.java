package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"full_name", "username", "photo", "role_name"})
public class UserResponseForJWT {
    @JsonProperty("user_id")
    private Integer id;

    @JsonProperty("full_name")
    private String fullName;

    private String username;

    private String email;

    private String photo;

    @JsonProperty("role_name")
    private String roleName;
}