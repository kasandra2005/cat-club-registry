package org.catclub.cat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.catclub.cat.client")
@EnableJpaRepositories(basePackages = "org.catclub.cat.repository")
@EntityScan(basePackages = {"org.catclub.cat.model", "org.catclub.owner.model"})
public class CatApplication {
    private static final Logger log = LoggerFactory.getLogger(CatApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(CatApplication.class, args);
        log.info("Liquibase should be initialized by Spring Boot auto-configuration");
    }
}