package com.example.bdhv_itclub.dto.reponse;

import com.example.bdhv_itclub.entity.LessonType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LessonReturnDetailResponse {

    private Integer id;

    private String name;

    @JsonProperty("lesson_type")
    private LessonType lessonType;

    private int orders;

    private LocalTime duration;
}
