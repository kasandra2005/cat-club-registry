package org.catclub.pedigree.config;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.micrometer.MicrometerCapability;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class FeignConfig {

    /**
     * Добавляем метрики для Feign-клиентов
     */
    @Bean
    public MicrometerCapability micrometerCapability(MeterRegistry registry) {
        return new MicrometerCapability(registry);
    }

    /**
     * Опциональный интерцептор (можно удалить, если не нужен)
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("X-Service-Name", "pedigree-service");
        };
    }

    /**
     * Кастомный обработчик ошибок
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignCustomErrorDecoder();
    }
}