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

    @NotEmpty(message = "Họ và tên không được để trống")
    @Length(min = 2, max = 45, message = "Họ và tên phải từ 2-45 ký tự")
    @JsonProperty("full_name")
    private String fullName;

    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Định dạng email không hợp lệ")
    @Length(min = 5, max = 50, message = "Email phải từ 5-50 ký tự")
    private String email;

    @NotEmpty(message = "Số điện thoại không được để trống")
    @Length(min = 10, max = 11, message = "Số điện thoại phải từ 10-11 ký tự")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotEmpty(message = "Nội dung phản hồi không được để trống")
    @Length(min = 10, message = "Nội dung phản hồi phải từ 10 ký tự")
    private String content;
}