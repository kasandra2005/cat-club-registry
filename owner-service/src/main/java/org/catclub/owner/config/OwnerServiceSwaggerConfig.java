package org.catclub.owner.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OwnerServiceSwaggerConfig {
    @Bean
    public OpenAPI ownerServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Owner Service API")
                        .description("API for managing cat owners")
                        .version("1.0"));
    }
}
