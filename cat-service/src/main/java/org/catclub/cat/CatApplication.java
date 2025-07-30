package org.catclub.cat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.catclub.cat.client")
public class CatApplication {
    private static final Logger log = LoggerFactory.getLogger(CatApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(CatApplication.class, args);
        log.info("Liquibase should be initialized by Spring Boot auto-configuration");
    }
}