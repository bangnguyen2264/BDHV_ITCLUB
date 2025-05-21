package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {

    private Integer id;

    @NotEmpty(message = "Content answer can not be empty")
    @Length(min = 4, max = 200, message = "Content answer must have 4 - 200 characters")
    private String content;

    @JsonProperty("is_correct")
    private boolean isCorrect;
}
