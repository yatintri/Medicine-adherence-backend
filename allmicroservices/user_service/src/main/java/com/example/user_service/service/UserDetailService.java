package com.example.user_service.service;

import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserDetails;


public interface UserDetailService {

    public UserDetails saveUserDetail(String id,UserDetails userDetails) throws UserexceptionMessage;
}
