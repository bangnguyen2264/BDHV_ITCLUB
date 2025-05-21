package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerLearningRequest {

    @JsonProperty("answer_id")
    private Integer id;

    @JsonProperty("content_perforate")
    private String contentPerforate;
}
