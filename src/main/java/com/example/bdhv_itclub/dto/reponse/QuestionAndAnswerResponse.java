package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "content", "lessonId", "user_id", "username", "user_photo", "created_at_formatted", "parent_id", "children"})
public class QuestionAndAnswerResponse {
    private Integer id;

    private String content;

    @JsonProperty("lesson_id")
    private Integer lessonId;

    @JsonProperty("user_id")
    private Integer userId;

    private String username;

    @JsonProperty("user_photo")
    private String userPhoto;

    @JsonProperty("role_name")
    private String roleName;

    @JsonProperty("created_at_formatted")
    private String createdAtFormatted;

    @JsonProperty("parent_id")
    private Integer parentId;

    private QuestionAndAnswerCourse course;

    private QuestionAndAnswerLesson lesson;

    private List<QuestionAndAnswerResponse> children = new ArrayList<>();

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionAndAnswerCourse {
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionAndAnswerLesson {
        private String name;
    }
}