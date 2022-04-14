package com.example.user_service.security;

import com.example.user_service.config.filter.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity

public class SecurityLayer extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailService userDetailService;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
    }




}