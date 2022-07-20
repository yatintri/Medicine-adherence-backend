package com.example.user_service.service.user;

import java.io.IOException;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import com.example.user_service.util.Constants;
import org.hibernate.exception.JDBCConnectionException;

import org.modelmapper.ModelMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.user_service.config.PdfMailSender;
import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.model.MedicineHistory;
import com.example.user_service.model.UserDetails;
import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.dto.UserEntityDTO;
import com.example.user_service.pojos.response.user.UserResponse;
import com.example.user_service.pojos.response.user.UserResponsePage;
import com.example.user_service.repository.UserDetailsRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.util.Datehelper;
import com.example.user_service.util.JwtUtil;

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

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, UserDetailsRepository userDetailsRepository,
                           ModelMapper mapper, PdfMailSender pdfMailSender, PasswordEncoder passwordEncoder,
                           UserMedicineRepository userMedicineRepository) {
        this.userRepository = userRepository;
        this.userMedicineRepository = userMedicineRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.mapper = mapper;
        this.pdfMailSender = pdfMailSender;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Logs in user
     */
    @Override
    public UserResponse login(String mail, String fcmToken) throws UserExceptionMessage, UserExceptions {
        logger.info(Constants.STARTING_METHOD_EXECUTION);

        try {
            UserEntity user = getUserByEmail(mail);
            UserDetails userDetails = user.getUserDetails();

            userDetails.setFcmToken(fcmToken);
            userDetailsRepository.save(userDetails);
            user = getUserByEmail(mail);

            if (user.getUserName() != null) {
                String jwtToken = jwtUtil.generateToken(user.getUserName());
                String refreshToken = passwordEncoder.encode(user.getUserId());

                logger.info(Constants.EXITING_METHOD_EXECUTION);
                logger.debug("Logging in with {} email",mail);
                return new UserResponse(Constants.SUCCESS,
                        Constants.SUCCESS,
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
            throws UserExceptionMessage, UserExceptions {
        logger.info(Constants.STARTING_METHOD_EXECUTION);

        try {
             checkForUser(userEntityDTO);
            logger.info(Thread.currentThread().getName());

            UserEntity userEntity = mapToEntity(userEntityDTO);

            userEntity.setLastLogin(Datehelper.getcurrentdatatime());
            userEntity.setCreatedAt(Datehelper.getcurrentdatatime());

            UserDetails userDetails = new UserDetails();

            userDetails.setFcmToken(fcmToken);
            userDetails.setPicPath(picPath);
            userDetails.setUser(userEntity);
            userEntity.setUserDetails(userDetails);

            UserEntity ue = userRepository.save(userEntity);

            if (ue.getUserName() == null) {
                logger.error("Save user: Error try again!");

                throw new UserExceptionMessage(Constants.ERROR);
            }

            String jwtToken = jwtUtil.generateToken(ue.getUserName());
            String refreshToken = passwordEncoder.encode(ue.getUserId());
            logger.info(Constants.EXITING_METHOD_EXECUTION);
            logger.debug("Saving {} user ", userEntityDTO);
            return new UserResponse(Constants.SUCCESS,
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

    private void checkForUser(UserEntityDTO userEntityDTO) throws UserExceptionMessage {
        if (userRepository.findByMail(userEntityDTO.getEmail())!= null) {
            throw new UserExceptionMessage(Constants.USER_PRESENT);
        }
    }

    /**
     * Fetches medicines by id and then generates pdf and sends it as a response
     */
    @Override
    public String sendUserMedicines(Integer medId)
            throws MessagingException, IOException, UserExceptions, UserExceptionMessage {
        logger.info(Constants.STARTING_METHOD_EXECUTION);

        try {
            Optional<UserMedicines> userMedicines = userMedicineRepository.findById(medId);

            if (userMedicines.isEmpty()) {
                return Constants.FAILED;
            }

            UserEntity entity = userMedicines.get().getUserEntity();
            List<MedicineHistory> medicineHistories = userMedicines.get().getMedicineHistories();
            logger.info(Constants.EXITING_METHOD_EXECUTION);
            logger.debug("Creating pdf for {} medicine",medId);
            return pdfMailSender.send(entity, userMedicines.get(), medicineHistories);
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Send user medicines :" + Constants.SQL_ERROR_MSG);
            throw new UserExceptionMessage(Constants.SQL_ERROR_MSG);
        }
    }

    /**
     * This method fetches user by its email ignoring the case
     */
    @Override

    @Cacheable(value = "userMail",key = "#email")
    public UserEntity getUserByEmail(String email) throws UserExceptionMessage, UserExceptions {
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
    public UserEntity getUserById(String userId) throws UserExceptionMessage {
        logger.info(Constants.STARTING_METHOD_EXECUTION);
        logger.info("Get by id");
        try {
            Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userRepository.getUserById(userId));

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
    public List<UserEntity> getUserByName(String userName) throws UserExceptionMessage, NullPointerException {
        logger.info(Constants.STARTING_METHOD_EXECUTION);

        try {
            List<UserEntity> userEntity = userRepository.findByNameIgnoreCase(userName);

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
    @Async
    public CompletableFuture<UserResponsePage> getUsers(int page, int limit)
            throws UserExceptionMessage, UserExceptions {

        logger.info(Constants.STARTING_METHOD_EXECUTION);
        Pageable pageableRequest = PageRequest.of(page, limit);

        try {
            Page<UserEntity> list = userRepository.findAllUsers(pageableRequest);

            logger.info(Thread.currentThread().getName());
            logger.info(Constants.EXITING_METHOD_EXECUTION);
            return CompletableFuture.completedFuture(new UserResponsePage(Constants.SUCCESS,
                    Constants.DATA_FOUND,
                    list.getTotalElements(),
                    list.getTotalPages(),
                    page,
                    list.getContent()));
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get users:" + Constants.SQL_ERROR_MSG);
            throw new UserExceptionMessage(Constants.SQL_ERROR_MSG);
        }
    }

    private UserEntity mapToEntity(UserEntityDTO userEntityDTO) {
        return mapper.map(userEntityDTO, UserEntity.class);
    }
}

