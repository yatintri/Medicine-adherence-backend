package com.example.user_service.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.user_service.service.UserDetailService;
import com.example.user_service.util.Constants;
import com.example.user_service.util.DateHelper;
import org.hibernate.exception.JDBCConnectionException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.CachePut;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.model.UserDetails;
import com.example.user_service.model.User;
import com.example.user_service.pojos.dto.request.UserDetailsDTO;
import com.example.user_service.repository.UserDetailsRepository;
import com.example.user_service.repository.UserRepository;

/**
 * This class contains all the business logic for the user detail controller
 */
@Service
public class UserDetailServiceImpl implements UserDetailService {
    Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    private final UserDetailsRepository userDetailsRepository;

    private final UserRepository userRepository;


    public UserDetailServiceImpl(UserDetailsRepository userDetailsRepository, UserRepository userRepository) {
        this.userDetailsRepository = userDetailsRepository;
        this.userRepository = userRepository;
    }

    /**
     * This class contains the business logic to save all the details of a user by its user id
     */
    @Override
    @CachePut(value = "userCache",key = "#id")
    public UserDetails saveUserDetail(String id, UserDetailsDTO userDetailsDTO)
           {
        logger.info(Constants.STARTING_METHOD_EXECUTION);

        try {
            Optional<User> user = Optional.ofNullable(userRepository.getUserById(id));

            if (user.isEmpty()) {
                logger.error("Save user details: User not found");

                throw new UserExceptionMessage(Constants.USER_NOT_FOUND);
            }

            UserDetails userDetails1 = user.get().getUserDetails();
            userDetails1.setUpdatedAt(DateHelper.getCurrentDatetime());
            userDetails1.setCreatedAt(LocalDateTime.now());
            userDetails1.setAge(userDetailsDTO.getAge());
            userDetails1.setBloodGroup(userDetailsDTO.getBloodGroup());
            userDetails1.setBio(userDetailsDTO.getBio());
            userDetails1.setGender(userDetailsDTO.getGender());
            userDetails1.setWeight(userDetailsDTO.getWeight());
            userDetails1.setMartialStatus(userDetailsDTO.getMartialStatus());
            userDetails1.setUserContact(userDetailsDTO.getUserContact());
            userDetailsRepository.save(userDetails1);
            logger.debug("Saving {} user {} details",id,userDetailsDTO);
            logger.info(Constants.EXITING_METHOD_EXECUTION);
            return userDetails1;
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(Constants.SQL_ERROR_MSG);
            throw new UserExceptionMessage(Constants.SQL_ERROR_MSG);
        }
    }
}

