package com.example.user_service.service;

import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserEntity;

import java.util.List;
import java.util.Optional;


public interface UserService {

    public UserEntity saveUser(UserEntity userEntity) throws UserexceptionMessage;

    public List<UserEntity> getUsers() throws UserexceptionMessage;

    public Optional<UserEntity> getUserById(Integer user_id) throws UserexceptionMessage;

    public  void deleteUserById(Integer user_id);

    public UserEntity updateUser(Integer user_id, UserEntity userEntity)throws UserexceptionMessage;

    public UserEntity getUserByName(String user_name)throws UserexceptionMessage;

    public UserEntity getUserByEmail(String email) throws UserexceptionMessage;
}
