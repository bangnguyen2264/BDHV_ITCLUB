package com.example.bdhv_itclub.dto.reponse;


import com.example.bdhv_itclub.constant.LessonType;
import com.example.bdhv_itclub.dto.request.LessonTextDTO;
import com.example.bdhv_itclub.dto.request.VideoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "lesson_type", "created_at", "chapter_id", "video"})
public class LessonResponse {
    private Integer id;

    private String name;

    @JsonProperty("lesson_type")
    private LessonType lessonType;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("chapter_id")
    private Integer chapterId;

    private VideoDTO video;

    private LessonTextDTO text;

    private List<QuizResponse> quizzes = new ArrayList<>();

    @JsonProperty("lesson_order")
    private int lessonOrder;
}