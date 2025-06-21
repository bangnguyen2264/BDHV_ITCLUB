package com.example.bdhv_itclub.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình Cloudinary để sử dụng trong toàn bộ ứng dụng.
 *
 * Lớp này chịu trách nhiệm khởi tạo và cấu hình đối tượng Cloudinary
 * dùng để tải lên/xoá/sửa ảnh trên dịch vụ Cloudinary.
 * Thông tin cấu hình được lấy từ file application.properties hoặc application.yml.
 */
@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure",true));
    }
}