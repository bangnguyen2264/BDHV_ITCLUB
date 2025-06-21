package com.example.bdhv_itclub.dto.reponse;

import com.example.bdhv_itclub.constant.CourseInformationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseInformationResponse {
    private Integer id;
    private String value;
    private CourseInformationType type;
}