package com.example.user_service.controller;


import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserEntity;
import com.example.user_service.service.MailService;
import com.example.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.SendFailedException;
import java.util.List;


@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    MailService mailService;


    @PostMapping(value = "/saveuser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@RequestBody UserEntity userEntity) throws UserexceptionMessage {

        return new ResponseEntity<>(userService.saveUser(userEntity), HttpStatus.CREATED);


    }

    @GetMapping(value = "/getusers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserEntity>> getUsers() throws UserexceptionMessage {

        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);


    }

    @GetMapping(value = "/getuser/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable("id") String user_id) throws UserexceptionMessage {

        return new ResponseEntity<>(userService.getUserById(user_id), HttpStatus.OK);

    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@PathVariable("id") String user_id
            , @RequestBody UserEntity userEntity) throws UserexceptionMessage {

        return new ResponseEntity<>(userService.updateUser(user_id, userEntity), HttpStatus.OK);


    }

    @GetMapping(value = "/getuser/byname", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserByName(@RequestParam("name") String user_name) throws UserexceptionMessage {

        return new ResponseEntity<>(userService.getUserByName(user_name), HttpStatus.OK);

    }

    @GetMapping(value = "/getbyemail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserByEmail(@RequestParam("email") String email)
                               throws UserexceptionMessage , SendFailedException {

       UserEntity userEntity = userService.getUserByEmail(email);
       if(userEntity == null){

          mailService.sendEmail(email);
          return new ResponseEntity<>("Invitation sent to user with given email id!" , HttpStatus.OK);
       }
        return new ResponseEntity<>(userEntity, HttpStatus.OK);

    }


}