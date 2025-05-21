package com.example.bdhv_itclub.dto.reponse;

import com.example.bdhv_itclub.entity.InformationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfoResponse {
    private Integer id;
    private String value;
    private InformationType type;
}
