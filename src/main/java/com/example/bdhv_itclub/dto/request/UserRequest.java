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
public class UserRequest {
    private Integer id;

    @NotEmpty(message = "Họ và tên không được để trống")
    @Length(min = 4, max = 64, message = "Họ và tên phải có 4-64 ký tự")
    @JsonProperty("full_name")
    private String fullName;

    @NotEmpty(message = "Tên tài khoản không được để trống")
    @Length(min = 4, max = 64, message = "Tên tài khoản phải có 5-45 ký tự")
    private String username;

    @Email(message = "Email không hợp lệ")
    @NotEmpty(message = "Email không được để trống")
    @Length(min = 15, max = 64, message = "Email phải có 15-64 ký tự")
    private String email;

    @NotEmpty(message = "Số điện thoại không được để trống")
    @Length(min = 10, max = 11, message = "Số điện thoại không đúng định dạng")
    @JsonProperty("phone_number")
    private String phoneNumber;

    private String password;

    private boolean enabled;

    private UserRole role;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class UserRole {
        private Integer id;
    }
}