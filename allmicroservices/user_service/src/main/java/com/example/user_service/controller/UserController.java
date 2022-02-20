package com.example.user_service.controller;


import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserEntity;
import com.example.user_service.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    //updated

    @PostMapping(value = "/saveuser" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@RequestBody UserEntity userEntity) throws UserexceptionMessage {
        try{
            return new ResponseEntity<>(userService.saveUser(userEntity) , HttpStatus.CREATED);

        }catch (UserexceptionMessage userexceptionMessage){
           throw new UserexceptionMessage("Error try again!");
        }
    }
    @GetMapping(value = "/getusers" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserEntity>> getUsers() throws UserexceptionMessage{
        try {
            return new ResponseEntity<>(userService.getUsers() , HttpStatus.OK);

        }catch (UserexceptionMessage userexceptionMessage){
             throw new UserexceptionMessage("cant fetch user try again!");
        }
    }

    @GetMapping(value = "/getuser/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById (@PathVariable("id") int user_id) throws UserexceptionMessage{
        try {
            return new ResponseEntity<>(userService.getUserById(user_id) , HttpStatus.OK);
        }catch (UserexceptionMessage userexceptionMessage){
            throw  new UserexceptionMessage("Cant find user with this id!");
        }
    }

    @PutMapping(value = "/update/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@PathVariable("id") Integer user_id
            , @RequestBody UserEntity userEntity)throws  UserexceptionMessage {
        try {

          return new ResponseEntity<>(userService.updateUser(user_id, userEntity) , HttpStatus.OK);

        }catch (UserexceptionMessage userexceptionMessage){
          throw new UserexceptionMessage("Error updating data!");
        }
    }

    @GetMapping(value = "/getuser/byname" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserByName(@RequestParam("name") String user_name) throws UserexceptionMessage
    {
        try {
            return new ResponseEntity<>(userService.getUserByName(user_name) , HttpStatus.OK);
        }catch (UserexceptionMessage userexceptionMessage){
            throw new UserexceptionMessage("User not available with this name!");
        }
    }

    @GetMapping(value = "/users/email" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserByEmail(@RequestParam("email") String email) throws UserexceptionMessage
    {
        try {
            return new ResponseEntity<>(userService.getUserByEmail(email),HttpStatus.OK);
        }catch (UserexceptionMessage userexceptionMessage){
            throw new UserexceptionMessage("User this email is not available");
        }
    }
}
