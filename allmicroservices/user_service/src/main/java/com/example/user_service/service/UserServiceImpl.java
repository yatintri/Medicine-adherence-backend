package com.example.user_service.service;


import com.example.user_service.config.PdfMailSender;
import com.example.user_service.exception.DataAccessExceptionMessage;
import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.model.MedicineHistory;
import com.example.user_service.model.UserDetails;
import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.dto.UserEntityDTO;
import com.example.user_service.pojos.response.UserResponse;
import com.example.user_service.pojos.response.UserResponsePage;
import com.example.user_service.repository.UserDetailsRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.util.Datehelper;

import com.example.user_service.util.JwtUtil;
import com.example.user_service.util.Messages;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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
        } catch (DataAccessException dataAccessException) {
            logger.error("Save user:" + Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }
    }

    @Override
    @Async
    public CompletableFuture<UserResponsePage> getUsers(int page, int limit) throws UserExceptionMessage , UserExceptions{

        logger.info("Get users");
        Pageable pageableRequest = PageRequest.of(page, limit);
        try {
            Page<UserEntity> list = userRepository.findAllUsers(pageableRequest);
            logger.info(Thread.currentThread().getName());
            return CompletableFuture.completedFuture(new UserResponsePage(Messages.SUCCESS,Messages.DATA_FOUND,list.getTotalElements(), list.getTotalPages(), page,list.getContent()));
        } catch (DataAccessException dataAccessException) {
            logger.error("Get users:"+ Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }
    }


    @Override

    public UserEntity getUserById(String userId) throws UserExceptionMessage, UserExceptions {

        logger.info("Get user by id");
        try {
            Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userRepository.getUserById(userId));

            logger.info(Thread.currentThread().getName());
            if (optionalUserEntity.isEmpty()) {
                throw new UserExceptionMessage(Messages.MSG);
            }

            return optionalUserEntity.get();
        } catch (DataAccessException dataAccessException) {
            logger.error("Get user by id:" + Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }
    }


    @Override
    public UserEntity updateUser(String userId, UserEntityDTO userEntityDTO) {

        logger.info("Update user");
        try {
            UserEntity userDB = userRepository.getUserById(userId);
            UserEntity userEntity = mapToEntity(userEntityDTO);
            if (Objects.nonNull(userEntity.getUserName()) && !"".equalsIgnoreCase(userEntity.getUserName())) {
                userDB.setUserName(userEntity.getUserName());
            }
            if (Objects.nonNull(userEntity.getEmail()) && !"".equalsIgnoreCase(userEntity.getEmail())) {
                userDB.setEmail(userEntity.getEmail());
            }

            return userRepository.save(userDB);
        } catch (DataAccessException dataAccessException) {
            logger.error("update user:" + Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }
    }

    @Override
    public List<UserEntity> getUserByName(String userName) throws UserExceptionMessage, NullPointerException, UserExceptions {

        logger.info("Get user by name");
        try {
            List<UserEntity> userEntity = userRepository.findByNameIgnoreCase(userName);
            if (userEntity.isEmpty()) {
                logger.error("Save user: Error try again!");

                throw new UserExceptionMessage(Messages.MSG);
            }
            return userEntity;
        } catch (DataAccessException dataAccessException) {
            logger.error("Get user by name" + Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }

    }

    @Override
    public UserEntity getUserByEmail(String email) throws UserExceptionMessage , UserExceptions{

        logger.info("Get user by email");
        try {
            return userRepository.findByMail(email);
        } catch (DataAccessException dataAccessException) {
            logger.error("Get user by email :" + Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }

    }


    @Override
    public String sendUserMedicines(Integer medId) throws MessagingException, IOException , UserExceptions{

        logger.info("Send user medicines ");
        try {
            Optional<UserMedicines> userMedicines = userMedicineRepository.findById(medId);
            if (userMedicines.isEmpty()) {

                return Messages.FAILED;
            }
            UserEntity entity = userMedicines.get().getUserEntity();
            List<MedicineHistory> medicineHistories = userMedicines.get().getMedicineHistories();
            return pdfMailSender.send(entity, userMedicines.get(), medicineHistories);
        } catch (DataAccessException dataAccessException) {
            logger.error("Send user medicines :" +Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage( Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }
    }

    @Override
    public UserResponse login(String mail, String fcmToken) throws UserExceptionMessage, UserExceptions {

        MDC.put("TRACE_ID", UUID.randomUUID().toString());

        logger.info("Login");
        try {
            UserEntity user = getUserByEmail(mail);
            UserDetails userDetails = user.getUserDetails();
            userDetails.setFcmToken(fcmToken);
            userDetailsRepository.save(userDetails);
            user = getUserByEmail(mail);
            if (user != null) {
                String jwtToken = jwtUtil.generateToken(user.getUserName());
                String refreshToken = passwordEncoder.encode(user.getUserId());
                return new UserResponse(Messages.SUCCESS, Messages.SUCCESS, new ArrayList<>(Arrays.asList(user)), jwtToken, refreshToken);
            }
            logger.error("LOGIN: Error logging in!");
            throw new UserExceptionMessage(Messages.MSG);

        } catch (DataAccessException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }
        catch (NullPointerException e){
            logger.error("LOGIN: Error try again!");

            throw new UserExceptionMessage(Messages.MSG);

        }


    }

    private UserEntity mapToEntity(UserEntityDTO userEntityDTO) {
        return mapper.map(userEntityDTO, UserEntity.class);
    }
}
