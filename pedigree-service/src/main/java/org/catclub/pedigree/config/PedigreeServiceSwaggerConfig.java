package org.catclub.pedigree.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedigreeServiceSwaggerConfig {
    @Bean
    public OpenAPI pedigreeServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pedigree Service API")
                        .description("API for managing cat's pedigree")
                        .version("1.0"));
    }
}
