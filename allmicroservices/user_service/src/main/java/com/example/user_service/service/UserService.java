package com.example.user_service.service;

import com.example.user_service.model.UserEntity;

import java.util.List;
import java.util.Optional;


public interface UserService {
    public UserEntity saveUser(UserEntity userEntity);

    public List<UserEntity> getUsers();

    public Optional<UserEntity> getUserById(Integer user_id);

    public  void deleteUserById(Integer user_id);

    public UserEntity updateUser(Integer user_id, UserEntity userEntity);

    public UserEntity getUserByName(String user_name);

    public UserEntity getUserByEmail(String email);
}
