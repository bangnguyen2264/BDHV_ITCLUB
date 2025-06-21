package com.example.bdhv_itclub.dto.request;

import com.example.bdhv_itclub.dto.reponse.LessonResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseChapterDTO {
    private Integer id;

    @NotEmpty
    @Length(min = 5, max = 190, message = "Tên chương học phải từ 5-190 ký tự")
    private String name;

    @JsonProperty("total_lesson")
    private int totalLesson;

    private List<LessonResponse> lessons;

    @JsonProperty("chapter_order")
    private int chapterOrder;
}