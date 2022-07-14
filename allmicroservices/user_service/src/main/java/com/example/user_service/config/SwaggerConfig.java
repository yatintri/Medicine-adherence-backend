package com.example.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

/**
 * This is config class for swagger api contract
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getInfo());
    }

    public ApiInfo getInfo(){

        return new ApiInfo(
                "Medicine adherence user_service REST API",
                "Some custom description of API.",
                "API version 1.0",
                "Terms of service",
                new Contact("Nineleaps", "www.example.com", "myeaddress@company.com"),
                "License of API", "API license URL", Collections.emptyList());

    }



}
//