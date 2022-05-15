package com.example.user_service.service;

import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.model.UserDetails;
import com.example.user_service.pojos.dto.UserDetailsDTO;


public interface UserDetailService {

    public UserDetails saveUserDetail(String id, UserDetailsDTO userDetailsDTO) throws  UserExceptionMessage;
}
///