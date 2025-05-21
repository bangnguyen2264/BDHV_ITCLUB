package com.example.bdhv_itclub.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlogRequest {

    private Integer id;

    @NotEmpty(message = "Title can not be empty")
    private String title;

    @NotEmpty(message = "Description can not be empty")
    private String description;

    @NotEmpty(message = "Content can not be empty")
    private String content;

    @NotNull(message = "User ID can not be null")
    @JsonProperty("user_id")
    private Integer userId;

}
