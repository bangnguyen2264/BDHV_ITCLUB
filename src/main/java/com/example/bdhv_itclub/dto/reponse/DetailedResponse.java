package com.example.bdhv_itclub.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lớp phản hồi tổng quát dùng để chuẩn hóa định dạng dữ liệu trả về từ API.
 *
 * @param <T> Kiểu dữ liệu của phần nội dung (payload) trong phản hồi.
 *
 * Các trường dữ liệu:
 * - status: Mã trạng thái HTTP (vd: 200, 400, 500,...).
 * - error: Mô tả lỗi nếu có lỗi xảy ra.
 * - message: Thông báo hoặc nội dung phản hồi.
 * - data: Dữ liệu chính được trả về (nếu có).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailedResponse<T> {
    private int status;
    private String error;
    private Object message;
    private T data;
}
