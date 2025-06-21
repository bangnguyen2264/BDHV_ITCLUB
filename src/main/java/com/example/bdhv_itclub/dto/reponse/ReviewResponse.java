package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Integer id;

    private String comment;

    private int rating;

    @JsonProperty("formatted_time")
    private String formattedTime;

    @JsonProperty("user_id")
    private Integer userId;

    private String username;

    @JsonProperty("user_photo")
    private String userPhoto;

    @JsonProperty("course_id")
    private Integer courseId;

    @JsonProperty("course_title")
    private String courseTitle;
}