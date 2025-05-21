package com.example.bdhv_itclub.dto.reponse;

import com.example.bdhv_itclub.entity.QuizType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizReturnInRecord {

    @JsonProperty("quiz_id")
    private Integer id;

    private String question;

    @JsonProperty("quiz_type")
    private QuizType quizType;

    private int order;

    @JsonProperty("is_correct_for_answer")
    private boolean isCorrectForAnswer;

    @JsonProperty("answer_list")
    private List<AnswerReturnInRecord> answerList = new ArrayList<>();
}
