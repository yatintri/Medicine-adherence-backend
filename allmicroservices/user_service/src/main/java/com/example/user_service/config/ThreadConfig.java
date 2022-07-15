package com.example.user_service.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * This is config class for running tasks asynchronously
 */
@Configuration
@EnableAsync
public class ThreadConfig {
    @Bean
    public Executor getExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(5);
        threadPoolTaskExecutor.setQueueCapacity(150);
        threadPoolTaskExecutor.setThreadNamePrefix("Thread :");
        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    }


}

