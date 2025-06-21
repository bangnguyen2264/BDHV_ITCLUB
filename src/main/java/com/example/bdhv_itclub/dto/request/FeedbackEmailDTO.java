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
public class FeedbackEmailDTO {
    @NotNull(message = "Phản hồi không được để trống")
    @JsonProperty("feedback_id")
    private Integer feedbackId;

    @NotEmpty(message = "Email không được để trống")
    @Length(min = 5, max = 50, message = "Email phải từ 5-50 ký tự")
    @Email
    @JsonProperty("email")
    private String toEmail;

    @NotEmpty(message = "Tiêu đề không được để trống")
    private String subject;

    @NotEmpty(message = "Nội dung không được để trống")
    private String content;
}