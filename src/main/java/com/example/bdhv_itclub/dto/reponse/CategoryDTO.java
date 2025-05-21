package com.example.bdhv_itclub.dto.reponse;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Integer id;

    @NotEmpty(message = "Thể loại không được để trống")
    @Length(min = 2, max = 45, message = "Tên thể loại phải có ít nhất 2-45 ký tự")
    private String name;

    private String slug;
}
