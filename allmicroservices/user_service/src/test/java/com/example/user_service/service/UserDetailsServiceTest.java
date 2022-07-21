package com.example.user_service.service;

import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.model.User;
import com.example.user_service.model.UserDetails;
import com.example.user_service.pojos.dto.request.UserDetailsDTO;
import com.example.user_service.repository.UserDetailsRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.impl.UserDetailServiceImpl;
import com.example.user_service.util.Constants;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {
    private UserDetailServiceImpl userDetailServiceImpl;
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @BeforeEach
    public void initCase(){
        userDetailServiceImpl = new UserDetailServiceImpl(userDetailsRepository,userRepository);
    }

    @Test
    @DisplayName("Save user detail Test")
    void saveUserDetailTest() throws UserExceptionMessage {
        UserDetailsDTO userDetailsDTO= new UserDetailsDTO("Something",21,null,"male","AB+","Unmarried",60);
        when(userRepository.getUserById("feyiafiafgiagfieagfi")).thenReturn(null);
        try {
            userDetailServiceImpl.saveUserDetail("feyiafiafgiagfieagfi", userDetailsDTO);
        }catch(UserExceptionMessage userExceptionMessage) {
            Assertions.assertEquals("User not found",userExceptionMessage.getMessage());
        }
    }

    @Test
    @DisplayName("Save user detail Exception Test")
    void saveUserDetailExceptionTest() throws UserExceptionMessage {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO("Something", 21, null, "male", "AB+", "Unmarried", 60);
        UserDetails userDetails = new UserDetails("73578dfd-e7c9-4381-a348-113e72d80fa2","something",null,21,null,414124,null,5L,56L,null,"male","AB+","Unmarried",60,2314,null,LocalDateTime.now(),LocalDateTime.now(),null);
        User user = new User("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),userDetails,null);
        when(userRepository.getUserById("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenReturn(user);
        UserDetails userDetails1= userDetailServiceImpl.saveUserDetail("73578dfd-e7c9-4381-a348-113e72d80fa2",userDetailsDTO);
        System.out.println(userDetails1);
        Assertions.assertEquals(userDetails.getUserDetailId(),userDetails1.getUserDetailId());
    }

    @Test
    @DisplayName("Save user detail SqlException Test")
    void saveUserDetailSqlExceptionTest() throws UserExceptionMessage {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO("Something", 21, null, "male", "AB+", "Unmarried", 60);
        UserDetails userDetails = new UserDetails("73578dfd-e7c9-4381-a348-113e72d80fa2", "something", null, 21, null, 414124, null, 5L, 56L, null, "male", "AB+", "Unmarried", 60, 2314, null,LocalDateTime.now(),LocalDateTime.now(), null);
        User user = new User("73578dfd-e7c9-4381-a348-113e72d80fa2", "vinay", "vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(), userDetails, null);
        when(userRepository.getUserById("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenThrow(JDBCConnectionException.class);
        try {
            userDetailServiceImpl.saveUserDetail("73578dfd-e7c9-4381-a348-113e72d80fa2", userDetailsDTO);
        } catch (UserExceptionMessage userExceptions) {

            Assertions.assertEquals(Constants.SQL_ERROR_MSG,userExceptions.getMessage());
        }

    }

}


