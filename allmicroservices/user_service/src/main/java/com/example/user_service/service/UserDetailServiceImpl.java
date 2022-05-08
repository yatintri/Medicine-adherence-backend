package com.example.user_service.service;

import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserDetails;
import com.example.user_service.model.UserEntity;
import com.example.user_service.pojos.dto.UserDetailsDTO;
import com.example.user_service.repository.UserDetailsRepository;
import com.example.user_service.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailService{

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;
    @Override
    public UserDetails saveUserDetail(String id, UserDetailsDTO userDetailsDTO) throws UserexceptionMessage {

        Optional<UserEntity> user = Optional.ofNullable(userRepository.getUserById(id));
        if (user.isEmpty()){
            throw new UserexceptionMessage("User not found");
        }
        UserDetails userDetails1 = user.get().getUserDetails();

        userDetails1.setAge(userDetailsDTO.getAge());
        userDetails1.setBloodGroup(userDetailsDTO.getBloodGroup());
        userDetails1.setBio(userDetailsDTO.getBio());
        userDetails1.setGender(userDetailsDTO.getGender());
        userDetails1.setWeight(userDetailsDTO.getWeight());
        userDetails1.setMartialStatus(userDetailsDTO.getMartialStatus());
        userDetails1.setUsercontact(userDetailsDTO.getUsercontact());
        return userDetailsRepository.save(userDetails1);



    }


}///