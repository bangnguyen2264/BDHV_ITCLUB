package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponse {
    private Integer id;

    @JsonProperty("enrolled_time")
    private Instant enrolledTime;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("course_name")
    private String courseName;
}