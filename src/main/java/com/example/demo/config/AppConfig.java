package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class AppConfig {

    @Value("${demo.cache.duration}")
    private int cacheDuration;

    @Value("${demo.cache.time-unit}")
    private TimeUnit timeUnit;

    @Bean
    @ConditionalOnProperty(
            value = "demo.cache.enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public CacheManager cacheManager() {

        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        cacheManager.setCaffeine(Caffeine.newBuilder()
            .expireAfterWrite(cacheDuration, timeUnit));

        return cacheManager;
    }

    @Bean
    public ObjectMapper objectMapper() { return new ObjectMapper(); }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}