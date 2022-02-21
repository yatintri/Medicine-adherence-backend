package com.example.user_service.service;

import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserDetails;
import com.example.user_service.model.UserEntity;
import com.example.user_service.repository.UserDetailsRepository;
import com.example.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserDetailServiceImpl implements UserDetailService{

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails saveUserDetail(String id,UserDetails userDetails) throws UserexceptionMessage {

    Optional<UserEntity> user = Optional.ofNullable(userRepository.getByid(id));

    if (user.isEmpty()){
        throw new UserexceptionMessage("No user found");
    }

    UserDetails userDetails1 = user.get().getUserDetails();
    userDetails1.setAddress(userDetails.getAddress());
    userDetails1.setAge(userDetails.getAge());
    userDetails1.setBlood_group(userDetails.getBlood_group());
    userDetails1.setBio(userDetails.getBio());
    userDetails1.setFcm_token(userDetails.getFcm_token());
    userDetails1.setLattitude(userDetails.getLattitude());
    userDetails1.setLongitude(userDetails.getLongitude());
    userDetails1.setGender(userDetails.getGender());
    userDetails1.setPic_path(userDetails.getPic_path());
    userDetails1.setGender(userDetails.getGender());
    userDetails1.setPast_medication(userDetails.getPast_medication());
    userDetails1.setMartial_status(userDetails.getMartial_status());
    userDetails1.setPincode(userDetails.getPincode());

   return userDetailsRepository.save(userDetails1);



    }

}
