package com.example.user_service.service;

import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserEntity;
import com.example.user_service.pojos.dto.UserEntityDTO;
import com.example.user_service.pojos.response.UserResponse;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public interface UserService {

     UserResponse saveUser(UserEntityDTO userEntityDTO, String fcmToken, String picPath) throws UserexceptionMessage;

     CompletableFuture<List<UserEntity>> getUsers() throws UserexceptionMessage;

     UserEntity getUserById(String userId) throws UserexceptionMessage, UserMedicineException, ExecutionException, InterruptedException;

     UserEntity updateUser(String userId, UserEntityDTO userEntityDTO)throws UserexceptionMessage;

     List<UserEntity> getUserByName(String userName)throws UserexceptionMessage;

     UserEntity getUserByEmail(String email) throws UserexceptionMessage;

     String sendUserMedicines(Integer userId) throws MessagingException, IOException;

}
//////