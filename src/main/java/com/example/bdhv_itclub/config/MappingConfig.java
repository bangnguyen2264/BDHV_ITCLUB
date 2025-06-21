package com.example.bdhv_itclub.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình ánh xạ đối tượng (object mapping) cho toàn bộ ứng dụng.
 *
 * Lớp này cung cấp bean ModelMapper dùng để tự động ánh xạ dữ liệu giữa các đối tượng
 * như Entity và DTO, giúp giảm thiểu việc viết code chuyển đổi thủ công.
 */
@Configuration
public class MappingConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}