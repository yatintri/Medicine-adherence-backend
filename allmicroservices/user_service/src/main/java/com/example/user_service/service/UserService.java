package com.example.user_service.service;

import com.example.user_service.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    public UserEntity saveUser(UserEntity userEntity);

    public List<UserEntity> getUsers();

    public UserEntity getUserById(Integer user_id);

    public  void deleteUserById(Integer user_id);

    public UserEntity updateUser(Integer user_id, UserEntity userEntity);

    public UserEntity getUserByName(String user_name);

    public UserEntity getUserByEmail(String email);
}
