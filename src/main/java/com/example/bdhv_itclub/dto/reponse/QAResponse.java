package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "content", "lessonId", "user_id", "username", "photo_user", "created_at_formatted", "parent_id", "children"})
public class QAResponse {

    private Integer id;

    private String content;

    @JsonProperty("lesson_id")
    private Integer lessonId;

    @JsonProperty("user_id")
    private Integer userId;

    private String username;

    @JsonProperty("photo_user")
    private String photoUser;

    @JsonProperty("role_name")
    private String roleName;

    @JsonProperty("created_at_formatted")
    private String createdAtFormatted;

    @JsonProperty("parent_id")
    private Integer parentId;

    private CourseQA course;

    private LessonQA lesson;

    private List<QAResponse> children = new ArrayList<>();

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourseQA {
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LessonQA {
        private String name;
    }
}
