package com.example.bdhv_itclub.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnswerLearningRequest {
    @JsonProperty("answer_id")
    private Integer id;

    @JsonProperty("perforated_content")
    private String perforatedContent;
}