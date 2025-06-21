package com.example.bdhv_itclub.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CourseRequest {
    private Integer id;

    @NotEmpty(message = "Tiêu đề không được để trống")
    @Length(min = 5, max = 60, message = "Tiêu đề phải từ 5-60 ký tự")
    private String title;

    private String slug;

    @NotEmpty(message = "Mô tả không được để trống")
    @Size(min = 10, message = "Mô tả phải có ít nhất 10 ký tự")
    private String description;

    private int price;

    private float discount;

    @JsonProperty("is_enabled")
    private boolean isEnabled;

    @JsonProperty("is_published")
    private boolean isPublished;

    @JsonProperty("is_finished")
    private boolean isFinished;

    @NotNull(message = "Mã danh mục khóa học không được để trống")
    @JsonProperty("category_id")
    private Integer categoryId;

    @NotEmpty(message = "Thông tin khóa học không được để trống")
    @Valid
    @JsonProperty("course_informations")
    private List<CourseInformationRequest> courseInformations;
}