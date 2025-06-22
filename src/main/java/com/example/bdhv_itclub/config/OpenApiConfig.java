package com.example.bdhv_itclub.config;

import com.example.bdhv_itclub.properties.OpenApiProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private final OpenApiProperties openApiProperties;

    public OpenApiConfig(OpenApiProperties openApiProperties) {
        this.openApiProperties = openApiProperties;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                        )
                )
                .info(new Info()
                        .title(openApiProperties.getTitle())
                        .version(openApiProperties.getVersion())
                        .description("OpenApi documentation for BHDV system")
                        .contact(new Contact()
                                .name("Nguyễn Trọng Bằng")
                                .email(openApiProperties.getContactEmail())
                        )
                        .license(new License()
                                .name("License")
                                .url("https://some-url.com")
                        )
                        .termsOfService("https://terms-of-service.url")
                )
                .addServersItem(new Server()
                        .url(openApiProperties.getBaseUrlApi())
                        .description("Local server")
                );
    }
}
