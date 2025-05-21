package com.example.bdhv_itclub.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
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
public class FeedbackRequest {

    private Integer id;

    @NotEmpty(message = "Full Name can not be empty")
    @Length(min = 2, max = 45, message = "Full name have must 2-45 characters")
    @JsonProperty("full_name")
    private String fullName;

    @NotEmpty(message = "Email can not be empty")
    @Email(message = "This is not format of email")
    @Length(min = 5, max = 50, message = "Email have must 5-50 characters")
    private String email;

    @NotEmpty(message = "Phone number can not be empty")
    @Length(min = 10, max = 11, message = "Phone number 10-11 characters")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotEmpty(message = "Content can not be empty")
    private String content;
}
