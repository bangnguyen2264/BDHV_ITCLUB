package com.example.bdhv_itclub.dto.request;

import jakarta.validation.constraints.NotEmpty;
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
public class CourseInformationRequest {
    private Integer id;

    @NotEmpty(message = "Thông tin khóa học không được để trống")
    @Length(min = 10, max = 255, message = "Thông tin khóa học phải từ 10-255 ký tự")
    private String value;

    @NotEmpty(message = "Loại thông tin khóa học không được để trống")
    @Pattern(
            regexp = "^(TARGET|REQUIREMENT)$",
            message = "Loại thông tin khóa học phải là TARGET hoặc REQUIREMENT"
    )
    private String type;
}