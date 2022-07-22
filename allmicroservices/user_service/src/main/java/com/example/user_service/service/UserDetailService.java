package com.example.user_service.service;

import com.example.user_service.model.UserDetails;
import com.example.user_service.pojos.request.UserDetailsDTO;

/**
 * This is an interface for user details service
 */
public interface UserDetailService {

     UserDetails saveUserDetail(String id, UserDetailsDTO userDetailsDTO) ;
}
