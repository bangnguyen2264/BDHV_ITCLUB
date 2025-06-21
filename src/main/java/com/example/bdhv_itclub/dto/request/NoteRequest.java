package com.example.bdhv_itclub.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequest {
    private Integer id;

    @NotEmpty
    private String content;

    @NotNull
    @JsonProperty("lesson_id")
    private Integer lessonId;

    @JsonProperty("recorded_time")
    private LocalTime recordedTime;
}