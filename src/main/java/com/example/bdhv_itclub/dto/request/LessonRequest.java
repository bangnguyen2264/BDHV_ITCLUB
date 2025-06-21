package com.example.bdhv_itclub.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LessonRequest {
    private Integer id;

    @NotEmpty(message = "Tên bài học không được để trống")
    @Length(min = 10, max = 100, message = "Tên bài học phải từ 10-100 ký tự")
    private String name;

    @NotEmpty(message = "Loại bài học không được để trống")
    @Pattern(
        regexp = "^(VIDEO|QUIZ|TEXT)$",
        message = "Loại bài học phải là 1 trong 3 loại: video, quiz, text"
    )
    @JsonProperty("lesson_type")
    private String lessonType;

    @NotNull(message = "Mã chương học không được để trống")
    @JsonProperty("chapter_id")
    private Integer chapterId;

    @JsonProperty("video_id")
    private Integer videoId;

    @JsonProperty("text_id")
    private Integer textId;

    @JsonProperty("lesson_order")
    private int lessonOrder;
}