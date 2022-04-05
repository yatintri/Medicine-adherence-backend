package com.example.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class ThreadConfig  {

    @Bean
    public Executor getexecutor(){

    ThreadPoolTaskExecutor threadPoolTaskExecutor =
                           new ThreadPoolTaskExecutor();

    threadPoolTaskExecutor.setCorePoolSize(5);
    threadPoolTaskExecutor.setMaxPoolSize(5);
    threadPoolTaskExecutor.setQueueCapacity(150);
    threadPoolTaskExecutor.setThreadNamePrefix("Thread :");
    threadPoolTaskExecutor.initialize();

    return threadPoolTaskExecutor;


}


}
