package com.example.aladinservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    OpenAPI aladinOpenAPI(@Value("${openapi.server-url:http://localhost:8000}") String serverUrl) {
        String bearerAuth = "BearerAuth";
        return new OpenAPI()
                .info(new Info().title("Aladin Service API").version("v1").description("알라딘 도서 조회 및 추천 API"))
                .addServersItem(new Server().url(serverUrl).description("API Gateway"))
                .components(new Components().addSecuritySchemes(bearerAuth,
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(bearerAuth));
    }
}
