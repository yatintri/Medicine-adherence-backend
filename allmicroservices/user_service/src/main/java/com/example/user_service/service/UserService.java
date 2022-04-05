package com.example.user_service.service;

import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public interface UserService {

    public CompletableFuture<UserEntity> saveUser(UserEntity userEntity,String fcmToken,String picPath) throws UserexceptionMessage;

    public CompletableFuture<List<UserEntity>> getUsers() throws UserexceptionMessage;

    public UserEntity getUserById(String userId) throws UserexceptionMessage, UserMedicineException, ExecutionException, InterruptedException;

    public UserEntity updateUser(String userId, UserEntity userEntity)throws UserexceptionMessage;

    public List<UserEntity> getUserByName(String userName)throws UserexceptionMessage;

    public UserEntity getUserByEmail(String email) throws UserexceptionMessage;
}
/////