package com.example.user_service.service.user;


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
import com.example.user_service.util.Messages;
import org.hibernate.exception.JDBCConnectionException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * This class contains all the business logic for the User controller
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    private final ModelMapper mapper;


    @Autowired
    private final PdfMailSender pdfMailSender;

    @Autowired
    UserMedicineRepository userMedicineRepository;

    @Autowired
    private final JwtUtil jwtUtil;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, UserDetailsRepository userDetailsRepository, ModelMapper mapper, PdfMailSender pdfMailSender, PasswordEncoder passwordEncoder, UserMedicineRepository userMedicineRepository) {
        this.userRepository= userRepository;
        this.userMedicineRepository=userMedicineRepository;
        this.userDetailsRepository =  userDetailsRepository;
        this.mapper = mapper; this.pdfMailSender = pdfMailSender;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;

    }

    /**
     * Saves a user and runs on a different thread using asynchronous programming
     */
    @Override
    public UserResponse saveUser(UserEntityDTO userEntityDTO, String fcmToken, String picPath) throws UserExceptionMessage ,UserExceptions {

        logger.info("Save user ");
        try {
            UserEntity user = getUserByEmail(userEntityDTO.getEmail());
            if (user != null) {
                return new UserResponse(Messages.FAILED, Messages.USER_PRESENT, new ArrayList<>(Arrays.asList(user)), "", "");
            }

            logger.info(Thread.currentThread().getName());
            UserEntity userEntity = mapToEntity(userEntityDTO);
            userEntity.setLastLogin(Datehelper.getcurrentdatatime());
            userEntity.setCreatedAt(Datehelper.getcurrentdatatime());
            UserDetails userDetails = new UserDetails();
            userDetails.setFcmToken(fcmToken);
            userDetails.setPicPath(picPath);
            userEntity.setUserDetails(userDetails);
            UserEntity ue = userRepository.save(userEntity);


            if (ue.getUserName() == null) {
                logger.error("Save user: Error try again!");
                throw new UserExceptionMessage(Messages.ERROR);

            }
            String jwtToken = jwtUtil.generateToken(ue.getUserName());
            String refreshToken = passwordEncoder.encode(ue.getUserId());

            return new UserResponse(Messages.SUCCESS, Messages.SAVED_USER, new ArrayList<>(Arrays.asList(ue)), jwtToken, refreshToken);
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Save user:" + Messages.SQL_ERROR_MSG);
            throw new UserExceptionMessage(Messages.SQL_ERROR_MSG);
        }
    }

    /**
     * Logs in user
     */
    @Override
    public UserResponse login(String mail, String fcmToken) throws UserExceptionMessage, UserExceptions {

        logger.info("Login");
        try {
            UserEntity user = getUserByEmail(mail);
            UserDetails userDetails = user.getUserDetails();
            userDetails.setFcmToken(fcmToken);
            userDetailsRepository.save(userDetails);
            user = getUserByEmail(mail);
            if (user.getUserName() != null) {
                String jwtToken = jwtUtil.generateToken(user.getUserName());
                String refreshToken = passwordEncoder.encode(user.getUserId());
                return new UserResponse(Messages.SUCCESS, Messages.SUCCESS, new ArrayList<>(Arrays.asList(user)), jwtToken, refreshToken);
            }
            logger.error("LOGIN: Error logging in!");
            throw new UserExceptionMessage(Messages.MSG);

        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);
            throw new UserExceptionMessage(Messages.SQL_ERROR_MSG );
        }

    }

    /**
     * Fetches all users and runs on a different thread using asynchronous programming
     */
    @Override
    @Async
    public CompletableFuture<UserResponsePage> getUsers(int page, int limit) throws UserExceptionMessage , UserExceptions{

        logger.info("Get users");
        Pageable pageableRequest = PageRequest.of(page, limit);
        try {
            Page<UserEntity> list = userRepository.findAllUsers(pageableRequest);
            logger.info(Thread.currentThread().getName());
            return CompletableFuture.completedFuture(new UserResponsePage(Messages.SUCCESS,Messages.DATA_FOUND,list.getTotalElements(), list.getTotalPages(), page,list.getContent()));
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get users:"+ Messages.SQL_ERROR_MSG);
            throw new UserExceptionMessage(Messages.SQL_ERROR_MSG);
        }
    }

    /**
     * This method return values that will be stored in the cache
     */
    @Override

    public UserEntity getUserById(String userId) throws UserExceptionMessage {

        logger.info("Get user by id");
        try {
            Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userRepository.getUserById(userId));

            logger.info(Thread.currentThread().getName());
            if (optionalUserEntity.isEmpty()) {
                throw new UserExceptionMessage(Messages.MSG);
            }

            return optionalUserEntity.get();
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get user by id:" + Messages.SQL_ERROR_MSG);
            throw new UserExceptionMessage(Messages.SQL_ERROR_MSG);
        }
    }

    /**
     * This method fetches user by its name ignoring the case
     */
    @Override
    public List<UserEntity> getUserByName(String userName) throws UserExceptionMessage, NullPointerException {

        logger.info("Get user by name");
        try {
            List<UserEntity> userEntity = userRepository.findByNameIgnoreCase(userName);
            if (userEntity.isEmpty()) {
                logger.error("Save user: Error try again!");

                throw new UserExceptionMessage(Messages.MSG);
            }
            return userEntity;
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get user by name" + Messages.SQL_ERROR_MSG);
            throw new UserExceptionMessage(Messages.SQL_ERROR_MSG );
        }

    }

    /**
     * This method fetches user by its email ignoring the case
     */
    @Override
    public UserEntity getUserByEmail(String email) throws UserExceptionMessage , UserExceptions{

        logger.info("Get user by email");
        try {
            return userRepository.findByMail(email);
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get user by email :" + Messages.SQL_ERROR_MSG);
            throw new UserExceptionMessage(Messages.SQL_ERROR_MSG );
        }

    }

    /**
     * Fetches medicines by id and then generates pdf and sends it as a response
     */
    @Override
    public String sendUserMedicines(Integer medId) throws MessagingException, IOException, UserExceptions, UserExceptionMessage {

        logger.info("Send user medicines ");
        try {
            Optional<UserMedicines> userMedicines = userMedicineRepository.findById(medId);
            if (userMedicines.isEmpty()) {

                return Messages.FAILED;
            }
            UserEntity entity = userMedicines.get().getUserEntity();
            List<MedicineHistory> medicineHistories = userMedicines.get().getMedicineHistories();
            return pdfMailSender.send(entity, userMedicines.get(), medicineHistories);
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Send user medicines :" +Messages.SQL_ERROR_MSG);
            throw new UserExceptionMessage( Messages.SQL_ERROR_MSG );
        }
    }



    private UserEntity mapToEntity(UserEntityDTO userEntityDTO) {
        return mapper.map(userEntityDTO, UserEntity.class);
    }
}
