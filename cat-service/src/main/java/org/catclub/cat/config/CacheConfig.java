package org.catclub.cat.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {

    @Primary
    @Bean("catCacheManager")
    public CacheManager catCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("cats");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(200)
                .recordStats());
        return cacheManager;
    }

    @Bean("pedigreeCacheManager")
    public CacheManager pedigreeCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("pedigrees");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(4, TimeUnit.HOURS)
                .maximumSize(100)
                .recordStats());
        return cacheManager;
    }

    @Override
    public CacheManager cacheManager() {
        return catCacheManager(); // Указываем основной CacheManager
    }
}