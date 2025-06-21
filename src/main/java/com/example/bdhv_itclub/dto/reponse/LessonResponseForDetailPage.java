package com.example.bdhv_itclub.dto.reponse;

import com.example.bdhv_itclub.constant.LessonType;
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
public class LessonResponseForDetailPage {
    private Integer id;

    private String name;

    @JsonProperty("lesson_type")
    private LessonType lessonType;

    @JsonProperty("lesson_order")
    private int lessonOrder;

    private LocalTime duration;
}