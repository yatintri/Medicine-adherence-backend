package com.example.user_service.service;

import com.example.user_service.exception.DataAccessExceptionMessage;
import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.model.UserDetails;
import com.example.user_service.model.UserEntity;
import com.example.user_service.pojos.dto.UserDetailsDTO;
import com.example.user_service.repository.UserDetailsRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.util.Messages;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);


    @Override
    public UserDetails saveUserDetail(String id, UserDetailsDTO userDetailsDTO) throws UserExceptionMessage, UserExceptions {
        try {
            Optional<UserEntity> user = Optional.ofNullable(userRepository.getUserById(id));
            if (user.isEmpty()) {
                throw new UserExceptionMessage(Messages.USER_NOT_FOUND);
            }
            UserDetails userDetails1 = user.get().getUserDetails();

            userDetails1.setAge(userDetailsDTO.getAge());
            userDetails1.setBloodGroup(userDetailsDTO.getBloodGroup());
            userDetails1.setBio(userDetailsDTO.getBio());
            userDetails1.setGender(userDetailsDTO.getGender());
            userDetails1.setWeight(userDetailsDTO.getWeight());
            userDetails1.setMartialStatus(userDetailsDTO.getMartialStatus());
            userDetails1.setUserContact(userDetailsDTO.getUserContact());
            return userDetailsRepository.save(userDetails1);
        } catch (DataAccessException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG+ dataAccessException.getMessage());
        }


    }


}