package com.example.user_service.controller;

import com.example.user_service.model.UserEntity;
import com.example.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/saveuser")
    public UserEntity saveUser(@RequestBody UserEntity userEntity) {
        return userService.saveUser(userEntity);
    }
    @GetMapping("/getusers")
    public List<UserEntity> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public UserEntity getUserById (@PathVariable("id")Integer user_id) {
        return userService.getUserById(user_id);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUserById(@PathVariable("id") Integer user_id)
    {
        userService.deleteUserById(user_id);
        return "User deleted successsfully";
    }

    @PutMapping("/users/{id}")
    public UserEntity updateUser(@PathVariable("id") Integer user_id, @RequestBody UserEntity userEntity) {
        return userService.updateUser(user_id, userEntity);

    }

    @GetMapping("/users/name/{name}")
    public UserEntity getUserByName(@PathVariable("name") String user_name)
    {
        return userService.getUserByName(user_name);
    }

    @GetMapping("/users/email/{email}")
    public UserEntity getUserByEmail(@PathVariable("email") String email)
    {
        return userService.getUserByEmail(email);
    }
}
