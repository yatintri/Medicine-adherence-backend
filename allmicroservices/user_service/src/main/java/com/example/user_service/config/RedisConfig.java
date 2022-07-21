package com.example.user_service.config;


import lombok.Setter;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Configuration
 @ConfigurationProperties(prefix = "spring.redis")
 @Setter
    public class RedisConfig {

        private String host;
        private String password;

        @Bean
        @Primary
        public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(RedisConfiguration defaultRedisConfig) {
            LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                    .useSsl().build();
            return new LettuceConnectionFactory(defaultRedisConfig, clientConfig);
        }

        @Bean
        public RedisConfiguration defaultRedisConfig() {
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
            config.setHostName(host);
            config.setPassword(RedisPassword.of(password));
            return config;
        }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> builder.withCacheConfiguration("userCache",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .disableCachingNullValues()
                                .entryTtl(Duration.ofMinutes(5)))
                .withCacheConfiguration("medicineCache",RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(15)))
                .withCacheConfiguration("caretakerCache",RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(15)))
                .withCacheConfiguration("userMail",RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(15)));
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

}

