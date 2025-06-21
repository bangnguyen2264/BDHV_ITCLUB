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
public class QuizAnswerResponseForRecord {
    @JsonProperty("answer_id")
    private Integer id;

    private String content;

    @JsonProperty("answer_is_correct")
    private boolean isCorrect;

    @JsonProperty("answer_of_customer")
    private boolean answerOfCustomer;

    @JsonProperty("perforated_content_of_customer")
    private String perforatedContentOfCustomer;
}