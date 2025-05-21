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
public class CourseInfoRequest {

    private Integer id;

    @NotEmpty(message = "Course info name can not be empty")
    @Length(min = 10, max = 255, message = "Course info name must have 10 - 255 characters")
    private String value;

    @NotEmpty(message = "Course info type can not be empty")
    @Pattern(
            regexp = "^(TARGET|REQUIREMENT)$",
            message = "Course info type must be one of: TARGET, REQUIREMENT"
    )
    private String type;
}
