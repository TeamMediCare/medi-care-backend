package com.careplan.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("복약 상담 API")
                        .description("복약 상담 서비스를 위한 REST API 문서")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("CarePlan")
                                .email("support@careplan.com")));
    }
}
