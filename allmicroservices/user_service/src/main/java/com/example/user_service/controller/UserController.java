package com.example.user_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uesrs")
public class UserController {
    @GetMapping("/1")
    public void show() {
        System.out.println("Showing uesrs.");
    }
}
