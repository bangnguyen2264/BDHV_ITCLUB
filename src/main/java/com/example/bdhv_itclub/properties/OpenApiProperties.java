package com.example.bdhv_itclub.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.api")
public class OpenApiProperties {
    private String title;
    private String version;
    private String contactEmail;
    private String baseUrlApi;
}