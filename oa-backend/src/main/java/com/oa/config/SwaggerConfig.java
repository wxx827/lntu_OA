package com.oa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OA API")
                        .version("1.0.0")
                        .description("OA?- V4.0")
                        .contact(new Contact()
                                .name("OA Team")
                                .email("support@oa.com")));
    }
}