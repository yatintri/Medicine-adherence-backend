package com.example.user_service.service.impl;

import java.io.FileNotFoundException;

import java.util.*;

import com.example.user_service.model.*;
import com.example.user_service.pojos.dto.response.RefreshTokenResponse;
import com.example.user_service.service.UserService;
import com.example.user_service.util.Constants;
import com.example.user_service.util.DateHelper;
import io.jsonwebtoken.ExpiredJwtException;
import org.hibernate.exception.JDBCConnectionException;

import org.modelmapper.ModelMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.user_service.config.PdfMailSender;
import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.pojos.dto.request.UserEntityDTO;
import com.example.user_service.pojos.dto.response.user.UserResponse;
import com.example.user_service.pojos.dto.response.user.UserResponsePage;
import com.example.user_service.repository.UserDetailsRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

import static com.example.user_service.util.Constants.*;

/**
 * This class contains all the business logic for the User controller
 */
@Service
public class UserServiceImpl implements UserService {
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final UserDetailsRepository userDetailsRepository;

    private final ModelMapper mapper;

    private final PdfMailSender pdfMailSender;

    UserMedicineRepository userMedicineRepository;

    private final JwtUtil jwtUtil;


    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, UserDetailsRepository userDetailsRepository,
                           ModelMapper mapper, PdfMailSender pdfMailSender,
                           UserMedicineRepository userMedicineRepository) {
        this.userRepository = userRepository;
        this.userMedicineRepository = userMedicineRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.mapper = mapper;
        this.pdfMailSender = pdfMailSender;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Logs in user
     */
    @Override
    public UserResponse login(String mail, String fcmToken) {
        logger.info(Constants.STARTING_METHOD_EXECUTION);

        try {
            User user = getUserByEmail(mail);
            UserDetails userDetails = user.getUserDetails();

            userDetails.setFcmToken(fcmToken);
            userDetailsRepository.save(userDetails);
            user = getUserByEmail(mail);

            if (user.getUserName() != null) {
                String jwtToken = jwtUtil.generateToken(user.getUserName());
                String refreshToken = jwtUtil.generateRefreshToken(user.getUserName());

                logger.info(Constants.EXITING_METHOD_EXECUTION);
                logger.debug("Logging in with {} email",mail);
                return new UserResponse(SUCCESS,
                        SUCCESS,
                        new ArrayList<>(Arrays.asList(user)),
                        jwtToken,
                        refreshToken);
            }

            logger.error("LOGIN: Error logging in!");

            throw new UserExceptionMessage(Constants.MSG);
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(Constants.SQL_ERROR_MSG);
            throw new UserExceptionMessage(Constants.SQL_ERROR_MSG);
        }
    }

    /**
     * Saves a user and runs on a different thread using asynchronous programming
     */
    @Override
    public UserResponse saveUser(UserEntityDTO userEntityDTO, String fcmToken, String picPath)
           {
        logger.info(Constants.STARTING_METHOD_EXECUTION);

        try {
             checkForUser(userEntityDTO);
            logger.info(Thread.currentThread().getName());

            User userEntity = mapToEntity(userEntityDTO);

            userEntity.setLastLogin(DateHelper.getCurrentDatetime());
            userEntity.setCreatedAt(DateHelper.getCurrentDatetime());

            UserDetails userDetails = new UserDetails();

            userDetails.setFcmToken(fcmToken);
            userDetails.setPicPath(picPath);
            userDetails.setUser(userEntity);
            userEntity.setUserDetails(userDetails);

            User ue = userRepository.save(userEntity);

            if (ue.getUserName() == null) {
                logger.error("Save user: Error try again!");

                throw new UserExceptionMessage(Constants.ERROR);
            }

            String jwtToken = jwtUtil.generateToken(ue.getUserName());
            String refreshToken = jwtUtil.generateRefreshToken(ue.getUserName());

            logger.info(Constants.EXITING_METHOD_EXECUTION);
            logger.debug("Saving {} user ", userEntityDTO);
            return new UserResponse(SUCCESS,
                    Constants.SAVED_USER,
                    new ArrayList<>(Arrays.asList(ue)),
                    jwtToken,
                    refreshToken);
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(Constants.SQL_ERROR_MSG);
            throw new UserExceptionMessage(Constants.SQL_ERROR_MSG);
        }
    }

    private void checkForUser(UserEntityDTO userEntityDTO)  {
        if (userRepository.findByMail(userEntityDTO.getEmail())!= null) {
            throw new UserExceptionMessage(Constants.USER_PRESENT);
        }
    }

    /**
     * Fetches medicines by id and then generates pdf and sends it as a response
     */
    @Override
    public String sendUserMedicines(Integer medId)
            {
        logger.info(STARTING_METHOD_EXECUTION);

        try {
            Optional<UserMedicines> userMedicines = userMedicineRepository.findById(medId);

            if (userMedicines.isEmpty()) {
                return FAILED;
            }

            User entity = userMedicines.get().getUserEntity();
            List<MedicineHistory> medicineHistories = userMedicines.get().getMedicineHistories();
            logger.info(EXITING_METHOD_EXECUTION);
            logger.debug("Creating pdf for {} medicine",medId);
            return pdfMailSender.send(entity, userMedicines.get(), medicineHistories);
        }
        catch (DataAccessException | JDBCConnectionException | FileNotFoundException dataAccessException) {
            logger.error("Send user medicines :" + SQL_ERROR_MSG);
            throw new UserExceptionMessage(SQL_ERROR_MSG);
        }
    }

    /**
     * This method fetches user by its email ignoring the case
     */
    @Override

    @Cacheable(value = "userMail",key = "#email")
    public User getUserByEmail(String email) {
        logger.info(Constants.STARTING_METHOD_EXECUTION);

        try {
            logger.debug("fetching {} mail",email);
            logger.info(Constants.EXITING_METHOD_EXECUTION);
            return userRepository.findByMail(email);
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get user by email :" + Constants.SQL_ERROR_MSG);
            throw new UserExceptionMessage(Constants.SQL_ERROR_MSG);
        }
    }

    /**
     * This method return values that will be stored in the cache
     */
    @Override
    @Cacheable(value = "userCache" ,key = "#userId")
    public User getUserById(String userId) {
        logger.info(Constants.STARTING_METHOD_EXECUTION);
        logger.info("Get by id");
        try {
            Optional<User> optionalUserEntity = Optional.ofNullable(userRepository.getUserById(userId));

            logger.info(Thread.currentThread().getName());

            if (optionalUserEntity.isEmpty()) {
                throw new UserExceptionMessage(Constants.MSG);
            }
            logger.debug("Fetching user by {} id",userId);
            logger.info(Constants.EXITING_METHOD_EXECUTION);
            return optionalUserEntity.get();
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get user by id:" + Constants.SQL_ERROR_MSG);

            throw new UserExceptionMessage(Constants.SQL_ERROR_MSG);
        }
    }

    /**
     * This method fetches user by its name ignoring the case
     */
    @Override
    public List<User> getUserByName(String userName) {
        logger.info(Constants.STARTING_METHOD_EXECUTION);

        try {
            List<User> userEntity = userRepository.findByNameIgnoreCase(userName);

            if (userEntity.isEmpty()) {
                logger.error("Save user: Error try again!");

                throw new UserExceptionMessage(Constants.MSG);
            }
            logger.debug("Fetching user by {} name",userName);
            logger.info(Constants.EXITING_METHOD_EXECUTION);
            return userEntity;
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get user by name :" + Constants.SQL_ERROR_MSG);
            throw new UserExceptionMessage(Constants.SQL_ERROR_MSG);
        }
    }

    /**
     * Fetches all users and runs on a different thread using asynchronous programming
     */
    @Override
//    @Async
    public UserResponsePage getUsers(int page, int limit)
            {

        logger.info(Constants.STARTING_METHOD_EXECUTION);
        Pageable pageableRequest = PageRequest.of(page, limit);

        try {
            Page<User> list = userRepository.findAllUsers(pageableRequest);

            logger.info(Thread.currentThread().getName());
            logger.info(Constants.EXITING_METHOD_EXECUTION);
            return new UserResponsePage(SUCCESS,
                    DATA_FOUND,
                    list.getTotalElements(),
                    list.getTotalPages(),
                    page,
                    list.getContent());

        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get users:" + SQL_ERROR_MSG);
            throw new UserExceptionMessage(SQL_ERROR_MSG);
        }
    }

    @Override
    public RefreshTokenResponse getRefreshToken(HttpServletRequest token, String userId)  {
        RefreshTokenResponse response = new RefreshTokenResponse();
        logger.debug("Generating new JWT token ");
        final String authHeader = token.getHeader("Authorization");
        String username = getUserById(userId).getUserName();
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            if(Boolean.TRUE.equals(checkRefreshToken(jwt,username))){
                logger.info("Valid refresh token, generating new jwt token");
                username = jwtUtil.extractUsername(jwt);
                response.setJwtToken(jwtUtil.generateToken(username));
                response.setRefreshToken(jwt.trim());
            }else{
                logger.info("Generating both jwt and refresh token");
                response.setJwtToken(jwtUtil.generateToken(username));
                response.setRefreshToken(jwtUtil.generateRefreshToken(username));
            }
            response.setStatus(SUCCESS);
            return response;
        } else {
            throw new UserExceptionMessage(INVALID_REFRESH_TOKEN);
        }

    }
    
    public Boolean checkRefreshToken(String jwt,String name) {
        try{
            logger.info("Validating refresh token");
            return jwtUtil.validateRefreshToken(jwt.trim(),name);
        }catch (ExpiredJwtException e){
            logger.info("Refresh token expired");
            return false;
        }
    }

    private User mapToEntity(UserEntityDTO userEntityDTO) {
        return mapper.map(userEntityDTO, User.class);
    }
}

