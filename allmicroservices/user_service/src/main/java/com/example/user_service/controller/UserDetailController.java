package com.example.user_service.controller;



import com.example.user_service.model.UserDetails;

import com.example.user_service.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userdetails")
public class UserDetailController {


    @Autowired
    private UserDetailService userDetailService;

    @PostMapping(value = "/updateuserdetails/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUserDetails(@PathVariable("id") int id, @RequestBody UserDetails userDetails) {
         return new ResponseEntity<>(userDetailService.saveUserDetail(id,userDetails),HttpStatus.CREATED);

    }

}
