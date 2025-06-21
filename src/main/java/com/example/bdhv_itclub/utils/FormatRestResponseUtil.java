package com.example.bdhv_itclub.utils;

import com.example.bdhv_itclub.dto.reponse.DetailedResponse;
import com.example.bdhv_itclub.utils.annotation.APIResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Chỉ áp dụng chuẩn hóa response cho các controller trong package api của bạn!
 * KHÔNG áp dụng cho Swagger, Actuator, hay các endpoint hệ thống.
 */
@RestControllerAdvice(basePackages = "com/example/bdhv_itclub/controller") // Đổi về package chứa controller của bạn!
public class FormatRestResponseUtil implements ResponseBodyAdvice<Object> {
    private final ObjectMapper objectMapper;

    public FormatRestResponseUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true; // Chỉ chạy với các controller đúng package, không cần check path!
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse httpServletResponse = null;
        if (response instanceof ServletServerHttpResponse) {
            httpServletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        }
        int status = (httpServletResponse != null) ? httpServletResponse.getStatus() : 200;

        if (status >= 400) {
            return body; // Trả nguyên trạng khi có lỗi
        }

        DetailedResponse<Object> responseDetail = new DetailedResponse<>();
        responseDetail.setStatus(status);

        APIResponseMessage message = returnType.getMethodAnnotation(APIResponseMessage.class);
        responseDetail.setMessage(message != null ? message.value() : "Gọi API thành công");
        responseDetail.setData(body);

        if (body instanceof String) {
            try {
                // Khi return String, cần trả JSON string
                return objectMapper.writeValueAsString(responseDetail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseDetail;
    }
}
