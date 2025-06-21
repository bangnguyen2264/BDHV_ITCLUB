package com.example.bdhv_itclub.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation tùy chỉnh dùng để gắn thông điệp phản hồi (message) cho các phương thức xử lý API.
 *
 * Khi được áp dụng, annotation này có thể được sử dụng bởi các interceptor, aspect hoặc cơ chế ghi log
 * để hiển thị hoặc xử lý thông điệp phản hồi tương ứng với hành động của API.
 *
 * - value: Nội dung thông điệp phản hồi (thường là mô tả hành động như "Thêm mới thành công", "Xóa thành công",...).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface APIResponseMessage {
    String value();
}