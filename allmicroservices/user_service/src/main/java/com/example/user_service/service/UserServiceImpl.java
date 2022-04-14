package com.example.user_service.service;

import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserDetails;
import com.example.user_service.model.UserEntity;
import com.example.user_service.repository.UserDetailsRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.util.Datehelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;


    @Autowired
    private UserMedicineService userMedicineService;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    @Async
    public CompletableFuture<UserEntity> saveUser(UserEntity userEntity, String fcmToken, String picPath) throws UserexceptionMessage {
//
        logger.info(Thread.currentThread().getName());
        userEntity.setLastLogin(Datehelper.getcurrentdatatime());
        userEntity.setCreatedAt(Datehelper.getcurrentdatatime());
        UserEntity ue = userRepository.save(userEntity);
        UserDetails userDetails = new UserDetails();
        userDetails.setFcmToken(fcmToken);
        userDetails.setPicPath(picPath);
        userDetails.setUser(ue);
        userDetailsRepository.save(userDetails);

        if (ue.getUserName() == null) {
            throw new UserexceptionMessage("Error try again!");

        }
        return CompletableFuture.completedFuture(ue);

    }

    @Override
    @Async
    public CompletableFuture<List<UserEntity>> getUsers() throws UserexceptionMessage {

        List<UserEntity> list = userRepository.findAllusers();
        logger.info(Thread.currentThread().getName());
        return CompletableFuture.completedFuture(list);

    }


    @Override

    public UserEntity getUserById(String userId) throws UserexceptionMessage, UserMedicineException, ExecutionException, InterruptedException {
        Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userRepository.getuserbyid(userId));

        logger.info(Thread.currentThread().getName());
        if (optionalUserEntity.isEmpty()) {
            throw new UserexceptionMessage("Not present with this id");
        }

        return optionalUserEntity.get();
    }


    @Override
    public UserEntity updateUser(String userId, UserEntity userEntity) {
        UserEntity userDB = userRepository.getuserbyid(userId);

        if (Objects.nonNull(userEntity.getUserName()) && !"".equalsIgnoreCase(userEntity.getUserName())) {
            userDB.setUserName(userEntity.getUserName());
        }
        if (Objects.nonNull(userEntity.getEmail()) && !"".equalsIgnoreCase(userEntity.getEmail())) {
            userDB.setEmail(userEntity.getEmail());
        }

        return userRepository.save(userDB);
    }

    @Override
    public List<UserEntity> getUserByName(String userName) throws UserexceptionMessage, NullPointerException {

        List<UserEntity> userEntity = userRepository.findByNameIgnoreCase(userName);
        if (userEntity.isEmpty()) {
            throw new UserexceptionMessage("User not available with this name!");
        }
        return userEntity;

    }

    @Override
    public UserEntity getUserByEmail(String email) throws UserexceptionMessage {

        return userRepository.findBymail(email);
    }
}
//////