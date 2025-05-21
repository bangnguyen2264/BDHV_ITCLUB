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
public class QARequest {

    private Integer id;

    @NotEmpty(message = "Content can not be empty!")
    private String content;

    @NotNull(message = "Lesson id can not be null")
    @JsonProperty("lesson_id")
    private Integer lessonId;

    @NotNull(message = "User id can not be null")
    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("parent_id")
    private Integer parentId;
}
