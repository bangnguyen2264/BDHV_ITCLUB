package com.example.bdhv_itclub.dto.reponse;

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
public class ChapterDTO {
    private Integer id;

    @NotEmpty
    @Length(min = 5, max = 190, message = "Chapter name must have 5 - 190 characters")
    private String name;

    @JsonProperty("total_lesson")
    private int totalLesson;

    private List<LessonResponse> lessonList;

    private int orders;
}
