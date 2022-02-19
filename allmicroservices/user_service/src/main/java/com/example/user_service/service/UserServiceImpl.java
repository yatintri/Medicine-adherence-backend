package com.example.user_service.service;

import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserEntity;
import com.example.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity saveUser(UserEntity userEntity) throws UserexceptionMessage {

       UserEntity userEntity1 =  userRepository.save(userEntity);
       if(userEntity1 == null){
           throw new UserexceptionMessage("Error try again!");

       }
       return userEntity1;

    }

    @Override
    public List<UserEntity> getUsers() throws UserexceptionMessage{
        return userRepository.findAll();
    }


    @Override
    @Cacheable(value = "user" , key="#user_id")
    public Optional<UserEntity> getUserById(Integer user_id) {
        System.out.println(user_id);
        return userRepository.findById(user_id);
    }


    @Override
    public UserEntity updateUser(Integer user_id, UserEntity userEntity) {
        UserEntity userDB = userRepository.getById(user_id);

        if(Objects.nonNull(userEntity.getUser_name()) && !"".equalsIgnoreCase(userEntity.getUser_name())) {
            userDB.setUser_name(userEntity.getUser_name());
        }
        if(Objects.nonNull(userEntity.getEmail()) && !"".equalsIgnoreCase(userEntity.getEmail())) {
            userDB.setEmail(userEntity.getEmail());
        }

        return userRepository.save(userDB);
    }

    @Override
    public UserEntity getUserByName(String user_name) throws UserexceptionMessage,NullPointerException{

        UserEntity userEntity = userRepository.findByNameIgnoreCase(user_name);
        if(userEntity == null){
            throw new UserexceptionMessage("User not available with this name!");
        }
        return userEntity;
    }

    @Override
    public UserEntity getUserByEmail(String email) throws UserexceptionMessage {

        UserEntity userEntity = userRepository.findByEmailIgnoreCase(email);
        if(userEntity == null){
            throw new UserexceptionMessage("Didnt find user with this email");
        }
        return userEntity;
    }
}
