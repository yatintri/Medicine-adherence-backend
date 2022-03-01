package com.example.user_service.service;

import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserDetails;
import com.example.user_service.model.UserEntity;
import com.example.user_service.repository.UserDetailsRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.util.Datehelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    @Async
    public CompletableFuture<UserEntity> saveUser(UserEntity userEntity) throws UserexceptionMessage {

        logger.info(Thread.currentThread().getName());
        userEntity.setLast_login(Datehelper.getcurrentdatatime());
        userEntity.setCreated_at(Datehelper.getcurrentdatatime());
        UserEntity ue = userRepository.save(userEntity);
        UserDetails userDetails = new UserDetails();
        userDetails.setUser(ue);
        userDetailsRepository.save(userDetails);

        if(ue == null){
           throw new UserexceptionMessage("Error try again!");

       }
       return CompletableFuture.completedFuture(ue);

    }

    @Override
    @Async
    public CompletableFuture<List<UserEntity>> getUsers() throws UserexceptionMessage{


         List<UserEntity> list = userRepository.findAllusers();
         logger.info(Thread.currentThread().getName());
         return CompletableFuture.completedFuture(list);
    }


    @Override
    @Cacheable(value = "user" , key="#user_id")
    public UserEntity getUserById(String user_id)throws UserexceptionMessage {
        Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userRepository.getByid(user_id));
        logger.info(Thread.currentThread().getName());
        if(optionalUserEntity.isEmpty()){
            throw new UserexceptionMessage("Not present with this id");
        }
        return optionalUserEntity.get();
    }


    @Override
    public UserEntity updateUser(String user_id, UserEntity userEntity) {
        UserEntity userDB = userRepository.getByid(user_id);

        if(Objects.nonNull(userEntity.getUser_name()) && !"".equalsIgnoreCase(userEntity.getUser_name())) {
            userDB.setUser_name(userEntity.getUser_name());
        }
        if(Objects.nonNull(userEntity.getEmail()) && !"".equalsIgnoreCase(userEntity.getEmail())) {
            userDB.setEmail(userEntity.getEmail());
        }

        return userRepository.save(userDB);
    }

    @Override
    public List<UserEntity> getUserByName(String user_name) throws UserexceptionMessage,NullPointerException{

        List<UserEntity> userEntity = userRepository.findByNameIgnoreCase(user_name);
        if(userEntity.isEmpty()){
            throw new UserexceptionMessage("User not available with this name!");
        }
        return userEntity;

    }

    @Override
    public UserEntity getUserByEmail(String email) throws UserexceptionMessage {

        UserEntity userEntity = userRepository.findByEmailIgnoreCase(email);

        return userEntity;
    }
}
