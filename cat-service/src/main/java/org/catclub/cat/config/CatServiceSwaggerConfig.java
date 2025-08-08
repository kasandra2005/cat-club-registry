package org.catclub.cat.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatServiceSwaggerConfig {
    @Bean
    public OpenAPI catServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cat Service API")
                        .description("API for managing cats")
                        .version("1.0"));
    }
}
