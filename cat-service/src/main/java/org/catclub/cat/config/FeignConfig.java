package org.catclub.cat.config;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Прокидывание заголовков авторизации
            ServletRequestAttributes attributes = (ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                String authorization = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
                if (authorization != null) {
                    requestTemplate.header(HttpHeaders.AUTHORIZATION, authorization);
                }
            }

            // Общие заголовки
            requestTemplate.header("X-Service-Name", "cat-service");
        };
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignCustomErrorDecoder();
    }

    // Конфигурация таймаутов (указывается в application.yml)
    /*
    @Bean
    public Feign.Builder feignBuilder(Retryer retryer) {
        return Feign.builder()
            .retryer(retryer)
            .options(new Request.Options(
                5000, // connectTimeout
                10000 // readTimeout
            ));
    }
    */
}
