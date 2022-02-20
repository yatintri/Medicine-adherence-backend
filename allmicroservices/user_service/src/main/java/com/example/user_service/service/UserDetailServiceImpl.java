package com.example.user_service.service;

import com.example.user_service.model.UserDetails;
import com.example.user_service.model.UserEntity;
import com.example.user_service.repository.UserDetailsRepository;
import com.example.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailService{

    @Autowired
    private UserDetailsRepository userDetailsRepository;
    //     Vinay is pro
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails saveUserDetail(int id,UserDetails userDetails) {
        UserEntity userEntity = userRepository.findById(id).get();
        userDetails.setUser(userEntity);
        return userDetailsRepository.save(userDetails);
    }
}
