package com.example.bdhv_itclub.dto.request;
import com.example.bdhv_itclub.constant.Sort;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserFilterRequest {

    @Schema(description = "Chuỗi tìm kiếm toàn cục", example = "bang")
    private String query;

    @Schema(description = "Tên field để sắp xếp", example = "fullName")
    private String field;

    @Schema(description = "Thứ tự sắp xếp", example = "asc" )
    private Sort sort;

}
