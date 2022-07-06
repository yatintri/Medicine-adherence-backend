package com.example.user_service.service;

import com.example.user_service.config.PdfMailSender;
import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.model.MedicineHistory;
import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.dto.DetailsDTO;
import com.example.user_service.pojos.dto.UserEntityDTO;
import com.example.user_service.pojos.response.UserResponse;
import com.example.user_service.pojos.response.UserResponsePage;
import com.example.user_service.repository.UserDetailsRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;

import com.example.user_service.util.JwtUtil;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class UserServiceTest {

    @InjectMocks
    UserServiceImpl userServiceimpl;

    @Mock
    UserRepository userRepository;
    @Mock
    UserDetailsRepository userDetailsRepository;
    @Mock
    ModelMapper mapper;
    @Mock
    PdfMailSender pdfMailSender;
//    @Mock
//    UserMedicineService userMedicineService;
    @Mock
    UserMedicineRepository userMedicineRepository;
    @Mock
    JwtUtil util;
    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup(){
        this.userServiceimpl= new UserServiceImpl(userRepository,util,userDetailsRepository,mapper,pdfMailSender,passwordEncoder,userMedicineRepository);
    }

    @Test
    void getUsersTest() throws Exception, UserExceptions {
        UserEntity userEntity = new UserEntity("sajhlk","vinay","vinay@gmail.com", LocalDateTime.now(),LocalDateTime.now(),null,null);
        UserEntity userEntity1 = new UserEntity("sjhaks","yatin","vinay@gmail.com",LocalDateTime.now(),LocalDateTime.now(),null,null);
        UserEntity userEntity2 = new UserEntity("diuaiuh","nikunj","vinay@gmail.com",LocalDateTime.now(),LocalDateTime.now(),null,null);
        List<UserEntity> users = new ArrayList<>();
        users.add(userEntity);
        users.add(userEntity1);
        users.add(userEntity2);
        int pageSize = 4;
        int pageNo = 0;
        Pageable paging = PageRequest.of(pageNo,pageSize);
        Page<UserEntity> userEntityPage= new PageImpl<>(users);
        when(userRepository.findAllUsers(paging)).thenReturn(userEntityPage);
        UserResponsePage userResponsePage = new UserResponsePage("Success","Data found",5L,2,1,userEntityPage.getContent());
        CompletableFuture<UserResponsePage> userResponsePage1=userServiceimpl.getUsers(0,4);
        Assertions.assertEquals(3,userResponsePage1.get().getUserEntityStream().size());
    }

    @Test
    void getUserByIdTest() throws UserExceptionMessage, UserExceptions {
        UserEntity userEntity = new UserEntity("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),null,null);
        when(userRepository.getUserById("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenReturn(userEntity);
        UserEntity userEntity1 = userServiceimpl.getUserById("73578dfd-e7c9-4381-a348-113e72d80fa2");
        Assertions.assertEquals(userEntity.getUserId(),userEntity1.getUserId());
    }

    @Test
    void getUserByNameTest() throws UserExceptionMessage, UserExceptions {
        UserEntity userEntity = new UserEntity("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),null,null);
        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(userEntity);
        when(userRepository.findByNameIgnoreCase(userEntity.getUserName())).thenReturn(userEntities);
        List<UserEntity> userEntities1 = userServiceimpl.getUserByName(userEntity.getUserName());
        System.out.println(userEntities1);
        Assertions.assertEquals(userEntities.get(0).getUserName(),userEntities1.get(0).getUserName());

    }

    @Test
    void getUserByEmail() throws UserExceptionMessage, UserExceptions {
        UserEntity userEntity = new UserEntity("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),null,null);
        when(userRepository.findByMail("vinay@gmail.com")).thenReturn(userEntity);
        UserEntity user = userServiceimpl.getUserByEmail(userEntity.getEmail());
        Assertions.assertEquals(userEntity.getUserId(),user.getUserId());
    }

    @Test
    void sendUserMedicinesTest1() throws UserExceptions, IOException {
        UserEntity userEntity = new UserEntity("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),null,null);
        MedicineHistory medicineHistory = new MedicineHistory(123,new Date(),"10:00 AM","8:00 PM",null);
        List<MedicineHistory> medicineHistoryList= new ArrayList<>();
        medicineHistoryList.add(medicineHistory);
        Optional<UserMedicines> userMedicines= Optional.of(new UserMedicines(12345, new Date(), "PCM", "for headache", "Mon", new Date(), "10:00 AM", "10 AM", 12, 12, userEntity, medicineHistoryList, null));
        when(userMedicineRepository.findById(12345)).thenReturn(userMedicines);
        UserEntity entity= userMedicines.get().getUserEntity();
        List<MedicineHistory> medicinesList= userMedicines.get().getMedicineHistories();
        String text= pdfMailSender.send(entity,userMedicines.get(),medicinesList);
        String text1= userServiceimpl.sendUserMedicines(12345);
        Assertions.assertEquals(text,text1);
    }

    @Test
    void saveUserTest() throws UserExceptionMessage, UserExceptions {

        UserEntity user = new UserEntity("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),null,null);
        UserResponse userResponse= new UserResponse("Failed","User is already present",new ArrayList<>(Arrays.asList(user)),null,null);
        when(userServiceimpl.getUserByEmail(user.getEmail())).thenReturn(null);
        UserEntityDTO userEntityDTO= new UserEntityDTO("vinay","vinay@gmail.com");
        when(mapper.map(userEntityDTO,UserEntity.class)).thenReturn(user);
        when(util.generateToken(user.getUserName())).thenReturn("fiasfiugaojfbjkabfk");
        when(passwordEncoder.encode(user.getUserId())).thenReturn("ujagfgouiaetfiugljgb");
        when(userRepository.save(user)).thenReturn(user);
        UserResponse userResponse1= userServiceimpl.saveUser(userEntityDTO,"eafyigfiagf","sfhoshgouahgo");
        Assertions.assertEquals(userResponse.getUserEntity().get(0).getUserName(),userResponse1.getUserEntity().get(0).getUserName());

    }

    @Test
    void saveUserTest1() throws UserExceptionMessage, UserExceptions {

        UserEntity user = new UserEntity("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),null,null);
        UserResponse userResponse= new UserResponse("Failed","User is already present",new ArrayList<>(Arrays.asList(user)),null,null);
        UserEntityDTO userEntityDTO= new UserEntityDTO("vinay","vinay@gmail.com");
        when(userServiceimpl.getUserByEmail(user.getEmail())).thenReturn(user);
        UserResponse userResponse1= userServiceimpl.saveUser(userEntityDTO,"eafyigfiagf","sfhoshgouahgo");
        Assertions.assertEquals(userResponse.getUserEntity().get(0).getUserName(),userResponse1.getUserEntity().get(0).getUserName());
    }

}





