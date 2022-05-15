package com.example.user_service.service;

import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.model.UserEntity;
import com.example.user_service.pojos.dto.UserEntityDTO;
import com.example.user_service.pojos.response.UserResponse;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public interface UserService {

     UserResponse saveUser(UserEntityDTO userEntityDTO, String fcmToken, String picPath) throws UserExceptionMessage;

     CompletableFuture<List<UserEntity>> getUsers() throws UserExceptionMessage;

     UserEntity getUserById(String userId) throws UserExceptionMessage, UserMedicineException, ExecutionException, InterruptedException;

     UserEntity updateUser(String userId, UserEntityDTO userEntityDTO)throws UserExceptionMessage;

     List<UserEntity> getUserByName(String userName)throws UserExceptionMessage;

     UserEntity getUserByEmail(String email) throws UserExceptionMessage;

     String sendUserMedicines(Integer userId) throws MessagingException, IOException;

     UserResponse login(String mail , String fcmToken) throws UserExceptionMessage;



}
