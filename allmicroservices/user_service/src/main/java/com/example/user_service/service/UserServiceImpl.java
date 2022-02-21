package com.example.user_service.service;

import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserDetails;
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

        UserDetails userDetails = new UserDetails();
        userEntity.setUserDetails(userDetails);
        UserEntity savedEntry =   userRepository.save(userEntity);
        if(userEntity == null || savedEntry == null){
           throw new UserexceptionMessage("Error try again!");

       }
       return savedEntry;

    }

    @Override
    public List<UserEntity> getUsers() throws UserexceptionMessage{
        return userRepository.findAll();
    }


    @Override
    @Cacheable(value = "user" , key="#user_id")
    public UserEntity getUserById(Integer user_id)throws UserexceptionMessage {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(user_id);
        if(optionalUserEntity.isEmpty()){
            throw new UserexceptionMessage("Not present with this id");
        }
        return optionalUserEntity.get();
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
        if(userEntity == null){
            throw new UserexceptionMessage("Didnt find user with this email");
        }
        return userEntity;
    }
}
//