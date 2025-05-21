package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {

    private Integer id;

    private String content;

    @JsonProperty("current_time")
    private LocalTime currentTime;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("lesson_id")
    private Integer lessonId;

    @JsonProperty("title_lesson")
    private String titleLesson;

    @JsonProperty("title_chapter")
    private String titleChapter;
}
