package com.example.user_service.service;

import com.example.user_service.config.PdfMailSender;
import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.model.UserEntity;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

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
    void saveCaretakerTest(){

    }

}





