package com.example.user_service.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a configuration class for authentication handler and interceptor
 */
@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public AuthenticationHandler authenticationHandler() {
        return new AuthenticationHandler();
    }


    private final Map<String, Bucket> cache = new HashMap<>();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(authenticationHandler())
                .addPathPatterns(
                        "/api/v1/email",
                        "/api/v1/users",
                        "/api/v1/request",
                        "/api/v1/accept",
                        "/api/v1/patients",
                        "/api/v1/patient/requests",
                        "/api/v1/caretakers",
                        "/api/v1/caretaker/requests",
                        "/api/v1/medicines/sync/**",
                        "/api/v1/userdetails"
                );
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                String ipAddress = request.getRemoteAddr();

                updateCache(ipAddress);

                if (!isRequestRateOk(ipAddress)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return false;
                }
             return true;
            }
        });
    }
    private void updateCache(String ipAddress) {
        cache.putIfAbsent(ipAddress,
                Bucket.builder()
                        .addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)))) // Set your request rate limit
                        .build());
    }
    private boolean isRequestRateOk(String ipAddress) {
        return cache.get(ipAddress).tryConsume(1);
    }

}
