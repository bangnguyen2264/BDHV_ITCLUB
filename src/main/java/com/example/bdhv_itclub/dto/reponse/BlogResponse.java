package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BlogResponse {
    private Integer id;

    private String title;

    private String slug;

    private String description;

    private String content;

    private String thumbnail;

    @JsonProperty("created_at_format")
    private String createdAtFormat;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("is_approved")
    private boolean isApproved;

    @JsonProperty("number_of_views")
    private int numberOfViews;

    private String username;

    @JsonProperty("user_avatar")
    private String userAvatar;
}