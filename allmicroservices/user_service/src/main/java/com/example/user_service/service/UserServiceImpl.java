package com.example.user_service.service;


import com.example.user_service.config.PdfMailSender;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.MedicineHistory;
import com.example.user_service.model.UserDetails;
import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.dto.UserEntityDTO;
import com.example.user_service.repository.UserDetailsRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.util.Datehelper;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Service
public class UserServiceImpl implements UserService{

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

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    @Async
    public CompletableFuture<UserEntity> saveUser(UserEntityDTO userEntityDTO,String fcmToken,String picPath) throws UserexceptionMessage {
//
        logger.info(Thread.currentThread().getName());
        UserEntity userEntity= mapToEntity(userEntityDTO);
        userEntity.setLastLogin(Datehelper.getcurrentdatatime());
        userEntity.setCreatedAt(Datehelper.getcurrentdatatime());
        UserEntity ue = userRepository.save(userEntity);
        UserDetails userDetails = new UserDetails();
        userDetails.setFcmToken(fcmToken);
        userDetails.setPicPath(picPath);
        userDetails.setUser(ue);
        userDetailsRepository.save(userDetails);

        if(ue.getUserName() == null){
            throw new UserexceptionMessage("Error try again!");

        }
        return CompletableFuture.completedFuture(ue);

    }

    @Override
    @Async
    public CompletableFuture<List<UserEntity>> getUsers() throws UserexceptionMessage{

        List<UserEntity> list = userRepository.findAllUsers();
        logger.info(Thread.currentThread().getName());
        return CompletableFuture.completedFuture(list);

    }


    @Override

    public UserEntity getUserById(String userId) throws UserexceptionMessage{
        Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userRepository.getUserById(userId));

        logger.info(Thread.currentThread().getName());
        if(optionalUserEntity.isEmpty()){
            throw new UserexceptionMessage("Data not found");
        }

        return optionalUserEntity.get();
    }


    @Override
    public UserEntity updateUser(String userId, UserEntityDTO userEntityDTO) {
        UserEntity userDB = userRepository.getUserById(userId);
        UserEntity userEntity= mapToEntity(userEntityDTO);
        if(Objects.nonNull(userEntity.getUserName()) && !"".equalsIgnoreCase(userEntity.getUserName())) {
            userDB.setUserName(userEntity.getUserName());
        }
        if(Objects.nonNull(userEntity.getEmail()) && !"".equalsIgnoreCase(userEntity.getEmail())) {
            userDB.setEmail(userEntity.getEmail());
        }

        return userRepository.save(userDB);
    }

    @Override
    public List<UserEntity> getUserByName(String userName) throws UserexceptionMessage,NullPointerException{

        List<UserEntity> userEntity = userRepository.findByNameIgnoreCase(userName);
        if(userEntity.isEmpty()){
            throw new UserexceptionMessage("Data not found");
        }
        return userEntity;

    }

    @Override
    public UserEntity getUserByEmail(String email) throws UserexceptionMessage {

        return userRepository.findByMail(email);

    }

    @Override
    public String sendUserMedicines(Integer medId) throws MessagingException, IOException {
        UserMedicines userMedicines = userMedicineRepository.findById(medId).get();
        UserEntity entity = userMedicines.getUserEntity();
        List<MedicineHistory> medicineHistories = userMedicines.getMedicineHistories();
        return pdfMailSender.send(entity,userMedicines,medicineHistories);
    }

    private UserEntity mapToEntity(UserEntityDTO userEntityDTO){
        return  mapper.map(userEntityDTO, UserEntity.class);
    }
}
//////