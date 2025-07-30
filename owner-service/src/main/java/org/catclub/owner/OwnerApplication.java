package org.catclub.owner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class OwnerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OwnerApplication.class, args);
    }
}
