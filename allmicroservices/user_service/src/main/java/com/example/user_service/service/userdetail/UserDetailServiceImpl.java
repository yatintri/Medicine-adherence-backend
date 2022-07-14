package com.example.user_service.service.userdetail;

import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.model.UserDetails;
import com.example.user_service.model.UserEntity;
import com.example.user_service.pojos.dto.UserDetailsDTO;
import com.example.user_service.repository.UserDetailsRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.util.Messages;
import org.hibernate.exception.JDBCConnectionException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class contains all the business logic for the user detail controller
 */
@Service
public class UserDetailServiceImpl implements UserDetailService {

    @Autowired
    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    public UserDetailServiceImpl(UserDetailsRepository userDetailsRepository, UserRepository userRepository) {
        this.userDetailsRepository = userDetailsRepository;
        this.userRepository = userRepository;
    }

    /**
     * This class contains the business logic to save all the details of a user by its user id
     */
    @Override
    public UserDetails saveUserDetail(String id, UserDetailsDTO userDetailsDTO) throws UserExceptionMessage, UserExceptions {

        logger.info("Save user details");
        try {
            Optional<UserEntity> user = Optional.ofNullable(userRepository.getUserById(id));
            if (user.isEmpty()) {
                logger.error("Save user details: User not found");
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
            userDetailsRepository.save(userDetails1);
            return userDetails1;
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);
            throw new UserExceptionMessage(Messages.SQL_ERROR_MSG);
        }


    }


}