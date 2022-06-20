package com.example.user_service.service;


import com.example.user_service.config.PdfMailSender;
import com.example.user_service.exception.DataAccessExceptionMessage;
import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.model.MedicineHistory;
import com.example.user_service.model.UserDetails;
import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.dto.UserEntityDTO;
import com.example.user_service.pojos.response.UserResponse;
import com.example.user_service.repository.UserDetailsRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.util.Datehelper;

import com.example.user_service.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private ModelMapper mapper;


    @Autowired
    private PdfMailSender pdfMailSender;

    @Autowired
    private UserMedicineService userMedicineService;

    @Autowired
    UserMedicineRepository userMedicineRepository;
    private static final String MSG = "Success";
    private static final String MSG2 = "failed";
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String errorMsg = "SQL error!";
    private static final String exception = "Data not found";


    @Override
    public UserResponse saveUser(UserEntityDTO userEntityDTO, String fcmToken, String picPath) throws UserExceptionMessage {

        try {
            UserEntity user = getUserByEmail(userEntityDTO.getEmail());
            if (user != null) {
                return new UserResponse(MSG2, "User is already present", new ArrayList<>(Arrays.asList(user)), "", "");
            }

            logger.info(Thread.currentThread().getName());
            UserEntity userEntity = mapToEntity(userEntityDTO);
            userEntity.setLastLogin(Datehelper.getcurrentdatatime());
            userEntity.setCreatedAt(Datehelper.getcurrentdatatime());
            UserEntity ue = userRepository.save(userEntity);
            UserDetails userDetails = new UserDetails();
            userDetails.setFcmToken(fcmToken);
            userDetails.setPicPath(picPath);
            userDetails.setUser(ue);
            userDetailsRepository.save(userDetails);

            if (ue.getUserName() == null) {
                throw new UserExceptionMessage("Error try again!");

            }
            String jwtToken = jwtUtil.generateToken(ue.getUserName());
            String refreshToken = passwordEncoder.encode(ue.getUserId());

            return new UserResponse(MSG, "Saved user successfully", new ArrayList<>(Arrays.asList(ue)), jwtToken, refreshToken);
        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }
    }

    @Override
    @Async
    public CompletableFuture<List<UserEntity>> getUsers() throws UserExceptionMessage {

        try {
            List<UserEntity> list = userRepository.findAllUsers();
            logger.info(Thread.currentThread().getName());
            return CompletableFuture.completedFuture(list);
        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }
    }


    @Override

    public UserEntity getUserById(String userId) throws UserExceptionMessage {
        try {
            Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userRepository.getUserById(userId));

            logger.info(Thread.currentThread().getName());
            if (optionalUserEntity.isEmpty()) {
                throw new UserExceptionMessage(exception);
            }

            return optionalUserEntity.get();
        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }
    }


    @Override
    public UserEntity updateUser(String userId, UserEntityDTO userEntityDTO) {
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
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }
    }

    @Override
    public List<UserEntity> getUserByName(String userName) throws UserExceptionMessage, NullPointerException {

        try {
            List<UserEntity> userEntity = userRepository.findByNameIgnoreCase(userName);
            if (userEntity.isEmpty()) {
                throw new UserExceptionMessage(exception);
            }
            return userEntity;
        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }

    }

    @Override
    public UserEntity getUserByEmail(String email) throws UserExceptionMessage {

        try {
            return userRepository.findByMail(email);
        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }

    }


    @Override
    public String sendUserMedicines(Integer medId) throws MessagingException, IOException {
        try {
            Optional<UserMedicines> userMedicines = userMedicineRepository.findById(medId);
            if (userMedicines.isEmpty()) {
                return "Failed";
            }
            UserEntity entity = userMedicines.get().getUserEntity();
            List<MedicineHistory> medicineHistories = userMedicines.get().getMedicineHistories();
            return pdfMailSender.send(entity, userMedicines.get(), medicineHistories);
        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }
    }

    @Override
    public UserResponse login(String mail, String fcmToken) throws UserExceptionMessage {
        try {
            UserEntity user = getUserByEmail(mail);
            UserDetails userDetails = user.getUserDetails();
            userDetails.setFcmToken(fcmToken);
            userDetailsRepository.save(userDetails);
            user = getUserByEmail(mail);
            if (user != null) {
                String jwtToken = jwtUtil.generateToken(user.getUserName());
                String refreshToken = passwordEncoder.encode(user.getUserId());
                return new UserResponse(MSG, "Success", new ArrayList<>(Arrays.asList(user)), jwtToken, refreshToken);
            }
            throw new UserExceptionMessage(exception);

        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }
        catch (NullPointerException e){
            throw new UserExceptionMessage(exception);

        }


    }

    private UserEntity mapToEntity(UserEntityDTO userEntityDTO) {
        return mapper.map(userEntityDTO, UserEntity.class);
    }
}
