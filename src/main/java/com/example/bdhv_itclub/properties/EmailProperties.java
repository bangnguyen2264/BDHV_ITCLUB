package com.example.bdhv_itclub.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {
    private String host;
    private int port;
    private String username;
    private String password;

    private final Map<String, String> properties = new HashMap<>();

}
