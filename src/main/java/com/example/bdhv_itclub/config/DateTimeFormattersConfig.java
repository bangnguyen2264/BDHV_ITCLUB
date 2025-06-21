package com.example.bdhv_itclub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Cấu hình định dạng ngày giờ mặc định trong toàn bộ ứng dụng.
 *
 * Lớp này sử dụng WebMvcConfigurer để đăng ký DateTimeFormatterRegistrar,
 * đảm bảo rằng các kiểu dữ liệu ngày giờ (LocalDate, LocalDateTime,...)
 * sẽ được định dạng theo chuẩn ISO (ISO-8601) khi xử lý với Spring MVC.
 */
@Configuration
public class DateTimeFormattersConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry formatterRegistry) {
        DateTimeFormatterRegistrar dateTimeFormatterRegistrar = new DateTimeFormatterRegistrar();
        dateTimeFormatterRegistrar.setUseIsoFormat(true);
        dateTimeFormatterRegistrar.registerFormatters(formatterRegistry);
    }
}