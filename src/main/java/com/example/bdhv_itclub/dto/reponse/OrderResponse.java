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
public class OrderResponse {

    private Integer id;

    @JsonProperty("created_time")
    private Instant createdTime;

    @JsonProperty("total_price")
    private int totalPrice;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("course_name")
    private String courseName;
}
