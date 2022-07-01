package com.example.user_service.service;

import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.model.UserEntity;
import com.example.user_service.pojos.dto.UserEntityDTO;
import com.example.user_service.pojos.response.UserResponse;
import com.example.user_service.pojos.response.UserResponsePage;
import org.springframework.messaging.MessagingException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public interface UserService {

     UserResponse saveUser(UserEntityDTO userEntityDTO, String fcmToken, String picPath) throws UserExceptionMessage , UserExceptions;

     CompletableFuture<UserResponsePage> getUsers(int page, int limit) throws UserExceptionMessage, UserExceptions;

     UserEntity getUserById(String userId) throws UserExceptionMessage, UserMedicineException, ExecutionException, InterruptedException, UserExceptions;

     UserEntity updateUser(String userId, UserEntityDTO userEntityDTO)throws UserExceptionMessage, UserExceptions;

     List<UserEntity> getUserByName(String userName)throws UserExceptionMessage, UserExceptions;

     UserEntity getUserByEmail(String email) throws UserExceptionMessage, UserExceptions;

     String sendUserMedicines(Integer userId) throws MessagingException, IOException, UserExceptions;

     UserResponse login(String mail , String fcmToken) throws UserExceptionMessage, UserExceptions;



}
