package com.sharp.vn.its.management.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * The type Swagger config.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Custom open api open api.
     *
     * @return the open api
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ITS ManagementService API")
                        .version("1.0")
                        .description("API for managing ITS tasks")
                        .contact(new Contact()
                                .name("Hung Tran")
                                .email("hung271999@gmail.com")
                        )
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("MIT License")
                                .url("https://choosealicense.com/licenses/mit/")
                        )
                )
                .addServersItem(new Server()
                        .url("http://localhost:8080/api/v1")
                        .description("Server URL in Development environment")
                )
                .components(new Components()
                        .addSecuritySchemes("Bearer Auth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Bearer authentication using JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Auth")
                );
    }
}
