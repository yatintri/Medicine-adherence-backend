package com.example.user_service.service;

import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public interface UserService {

    public CompletableFuture<UserEntity> saveUser(UserEntity userEntity,String fcm_token,String pic_path) throws UserexceptionMessage;

    public CompletableFuture<List<UserEntity>> getUsers() throws UserexceptionMessage;

    public UserEntity getUserById(String user_id) throws UserexceptionMessage, UserMedicineException, ExecutionException, InterruptedException;

    public UserEntity updateUser(String user_id, UserEntity userEntity)throws UserexceptionMessage;

    public List<UserEntity> getUserByName(String user_name)throws UserexceptionMessage;

    public UserEntity getUserByEmail(String email) throws UserexceptionMessage;
}
/////