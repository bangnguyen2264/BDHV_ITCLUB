package com.example.bdhv_itclub.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SendEmail {

    @NotNull(message = "Feedback can not be null")
    @JsonProperty("feedback_id")
    private Integer feedbackId;

    @NotEmpty(message = "Email can not be empty")
    @Length(min = 5, max = 50, message = "Email must have 5-50 characters")
    @Email
    @JsonProperty("email")
    private String toEmail;

    @NotEmpty(message = "Subject can not be empty")
    private String subject;

    @NotEmpty(message = "Content can not be empty")
    private String content;
}
