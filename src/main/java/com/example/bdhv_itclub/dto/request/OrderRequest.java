package com.example.bdhv_itclub.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @JsonProperty("course_id")
    @NotNull(message = "Course id can not be null")
    private Integer courseId;

    @JsonProperty("total_price")
    private int totalPrice;
}
