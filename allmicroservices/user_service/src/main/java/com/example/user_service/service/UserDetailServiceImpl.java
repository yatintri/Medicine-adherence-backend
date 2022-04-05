package com.example.user_service.service;

import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserDetails;
import com.example.user_service.model.UserEntity;
import com.example.user_service.repository.UserDetailsRepository;
import com.example.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailService{

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails saveUserDetail(String id,UserDetails userDetails) throws UserexceptionMessage {

    Optional<UserEntity> user = Optional.ofNullable(userRepository.getuserbyid(id));
    if (user.isEmpty()){
        throw new UserexceptionMessage("No user found");
    }
    UserDetails userDetails1 = user.get().getUserDetails();
    userDetails1.setAge(userDetails.getAge());
    userDetails1.setBloodGroup(userDetails.getBloodGroup());
    userDetails1.setBio(userDetails.getBio());
    userDetails1.setGender(userDetails.getGender());
    userDetails1.setGender(userDetails.getGender());
    userDetails1.setMartialStatus(userDetails.getMartialStatus());
    userDetails1.setUsercontact(userDetails.getUsercontact());
   return userDetailsRepository.save(userDetails1);



    }

}//
