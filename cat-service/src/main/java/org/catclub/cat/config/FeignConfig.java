package org.catclub.cat.config;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.micrometer.MicrometerCapability; // Измененный импорт
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class FeignConfig {

    /**
     * Включение метрик Feign
     */
    @Bean
    public MicrometerCapability micrometerCapability(MeterRegistry registry) {
        return new MicrometerCapability(registry);
    }

    /**
     * Базовый интерцептор (можно удалить, если не используется)
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("X-Service-Name", "cat-service");
        };
    }

    /**
     * Обработчик ошибок
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignCustomErrorDecoder();
    }
}