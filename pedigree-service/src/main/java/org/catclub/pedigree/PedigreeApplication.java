package org.catclub.pedigree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableCaching
@EnableFeignClients(basePackages = "org.catclub.pedigree.client")
public class PedigreeApplication {
    public static void main(String[] args) {
        SpringApplication.run(PedigreeApplication.class, args);
    }
}