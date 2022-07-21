package com.example.user_service.service;

import com.example.user_service.config.PdfMailSender;
import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.model.MedicineHistory;
import com.example.user_service.model.User;
import com.example.user_service.model.UserDetails;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.dto.request.UserEntityDTO;
import com.example.user_service.pojos.dto.response.user.UserResponse;
import com.example.user_service.pojos.dto.response.user.UserResponsePage;
import com.example.user_service.repository.UserDetailsRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;

import com.example.user_service.service.impl.UserServiceImpl;
import com.example.user_service.util.Constants;
import com.example.user_service.util.JwtUtil;

import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
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

    @Mock
    UserMedicineRepository userMedicineRepository;
    @Mock
    JwtUtil util;
    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup(){
        this.userServiceimpl= new UserServiceImpl(userRepository,util,userDetailsRepository,mapper,pdfMailSender,userMedicineRepository);
    }

    @Test
    @DisplayName("Get Users Test")
    void getUsersTest()  {
        User userEntity = new User("sajhlk","vinay","vinay@gmail.com", LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now(),null,null);
        User userEntity1 = new User("sjhaks","yatin","vinay@gmail.com",LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now(),null,null);
        User userEntity2 = new User("diuaiuh","nikunj","vinay@gmail.com",LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now(),null,null);
        List<User> users = new ArrayList<>();
        users.add(userEntity);
        users.add(userEntity1);
        users.add(userEntity2);
        int pageSize = 4;
        int pageNo = 0;
        Pageable paging = PageRequest.of(pageNo,pageSize);
        Page<User> userEntityPage= new PageImpl<>(users);
        when(userRepository.findAllUsers(paging)).thenReturn(userEntityPage);
        UserResponsePage userResponsePage = new UserResponsePage("Success","Data found",5L,2,1,userEntityPage.getContent());
        UserResponsePage userResponsePage1=userServiceimpl.getUsers(0,4);
        Assertions.assertEquals(3,userResponsePage1.getUserEntityStream().size());
    }

    @Test
    @DisplayName("Get Users SqlTest")
    void getUsersSqlTest()  {
        User userEntity = new User("sajhlk", "vinay", "vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(), null, null);
        User userEntity1 = new User("sjhaks", "yatin", "vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(), null, null);
        User userEntity2 = new User("diuaiuh", "nikunj", "vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(), null, null);
        List<User> users = new ArrayList<>();
        users.add(userEntity);
        users.add(userEntity1);
        users.add(userEntity2);
        int pageSize = 4;
        int pageNo = 0;
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<User> userEntityPage = new PageImpl<>(users);
        when(userRepository.findAllUsers(paging)).thenThrow(JDBCConnectionException.class);
        UserResponsePage userResponsePage = new UserResponsePage("Success", "Data found", 5L, 2, 1, userEntityPage.getContent());
        try {
            userServiceimpl.getUsers(0, 4);
        } catch (UserExceptionMessage userExceptionMessage) {
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,userExceptionMessage.getMessage());
        }
    }

    @Test
    @DisplayName("Get Users by id Test")
    void getUserByIdTest() throws UserExceptionMessage {
        User userEntity = new User("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),null,null);
        when(userRepository.getUserById("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenReturn(userEntity);
        User userEntity1 = userServiceimpl.getUserById("73578dfd-e7c9-4381-a348-113e72d80fa2");
        Assertions.assertEquals(userEntity.getUserId(),userEntity1.getUserId());
    }
    @Test
    @DisplayName("Get Users by id Exception Test")
    void getUserByIdTestException(){
        when(userRepository.getUserById("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenReturn(null);
        try{
            User userEntity1 = userServiceimpl.getUserById("73578dfd-e7c9-4381-a348-113e72d80fa2");
        } catch (UserExceptionMessage userExceptionMessage){
            Assertions.assertEquals("Data not found",userExceptionMessage.getMessage());
        }
    }

    @Test
    @DisplayName("Get Users by id SqlTest")
    void getUserByIdSqlTestException(){
        when(userRepository.getUserById("73578dfd-e7c9-4381-a348-113e72d80fa2")).thenThrow(JDBCConnectionException.class);
        try{
             userServiceimpl.getUserById("73578dfd-e7c9-4381-a348-113e72d80fa2");
        } catch (UserExceptionMessage userExceptionMessage){
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,userExceptionMessage.getMessage());
        }
    }



    @Test
    @DisplayName("Get Users by name Test")
    void getUserByNameTest() throws UserExceptionMessage{
        User userEntity = new User("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),null,null);
        List<User> userEntities = new ArrayList<>();
        userEntities.add(userEntity);
        when(userRepository.findByNameIgnoreCase(userEntity.getUserName())).thenReturn(userEntities);
        List<User> userEntities1 = userServiceimpl.getUserByName(userEntity.getUserName());
        System.out.println(userEntities1);
        Assertions.assertEquals(userEntities.get(0).getUserName(),userEntities1.get(0).getUserName());

    }
    @Test
    @DisplayName("Get Users by name Exception Test")
    void getUserByNameTestException() {
        List<User> userEntities = new ArrayList<>();
        when(userRepository.findByNameIgnoreCase(any())).thenReturn(userEntities);
        try {
            List<User> userEntities1 = userServiceimpl.getUserByName("vinay");
        }catch (UserExceptionMessage userExceptionMessage){
            Assertions.assertEquals("Data not found",userExceptionMessage.getMessage());
        }
    }

    @Test
    @DisplayName("Get Users by name SqlTest")
    void getUserByNameSqlTestException() {
        List<User> userEntities = new ArrayList<>();
        when(userRepository.findByNameIgnoreCase(any())).thenThrow(JDBCConnectionException.class);
        try {
            userServiceimpl.getUserByName("vinay");
        }catch (UserExceptionMessage userExceptionMessage){
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,userExceptionMessage.getMessage());
        }
    }

    @Test
    @DisplayName("Get Users by email Test")
    void getUserByEmail() throws UserExceptionMessage {
        User userEntity = new User("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),null,null);
        when(userRepository.findByMail("vinay@gmail.com")).thenReturn(userEntity);
        User user = userServiceimpl.getUserByEmail(userEntity.getEmail());
        Assertions.assertEquals(userEntity.getUserId(),user.getUserId());
    }

    @Test
    @DisplayName("Get Users by email sql Test")
    void getUserByEmailSqlTest() throws UserExceptionMessage {
        User userEntity = new User("73578dfd-e7c9-4381-a348-113e72d80fa2", "vinay", "vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(), null, null);
        when(userRepository.findByMail("vinay@gmail.com")).thenThrow(JDBCConnectionException.class);
        try {
            userServiceimpl.getUserByEmail(userEntity.getEmail());
        } catch (UserExceptionMessage userExceptionMessage) {
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,userExceptionMessage.getMessage());
        }
    }


    @Test
    @DisplayName("Send medicines Test")
    void sendUserMedicinesTest1() throws IOException, UserExceptionMessage {
        User userEntity = new User("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),null,null);
        MedicineHistory medicineHistory = new MedicineHistory(123,new Date(),"10:00 AM","8:00 PM",LocalDateTime.now(),LocalDateTime.now(),null);
        List<MedicineHistory> medicineHistoryList= new ArrayList<>();
        medicineHistoryList.add(medicineHistory);
        Optional<UserMedicines> userMedicines= Optional.of(new UserMedicines(12345, new Date(), "PCM", "for headache", "Mon", new Date(), "10:00 AM", "10 AM", 12, 12,LocalDateTime.now() ,LocalDateTime.now(),userEntity, medicineHistoryList, null));
        when(userMedicineRepository.findById(12345)).thenReturn(userMedicines);
        User entity= userMedicines.get().getUserEntity();
        List<MedicineHistory> medicinesList= userMedicines.get().getMedicineHistories();
        String text= pdfMailSender.send(entity,userMedicines.get(),medicinesList);
        String text1= userServiceimpl.sendUserMedicines(12345);
        Assertions.assertEquals(text,text1);
    }
    @Test
    @DisplayName("Send medicines Exception Test")
    void sendUserMedicinesExceptionTest(){
        User userEntity = new User("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),null,null);
        MedicineHistory medicineHistory = new MedicineHistory(123,new Date(),"10:00 AM","8:00 PM",LocalDateTime.now(),LocalDateTime.now(),null);
        List<MedicineHistory> medicineHistoryList= new ArrayList<>();
        medicineHistoryList.add(medicineHistory);
        Optional<UserMedicines> userMedicines= Optional.empty();
        when(userMedicineRepository.findById(12345)).thenReturn(userMedicines);
        try {
            userServiceimpl.sendUserMedicines(12345);
        } catch (UserExceptionMessage e) {
            Assertions.assertEquals("Data not found",e.getMessage());
        }
    }

    @Test
    @DisplayName("Send user medicines SqlTest")
    void sendUserMedicinesSqlExceptionTest(){
        User userEntity = new User("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),null,null);
        MedicineHistory medicineHistory = new MedicineHistory(123,new Date(),"10:00 AM","8:00 PM",LocalDateTime.now(),LocalDateTime.now(),null);
        List<MedicineHistory> medicineHistoryList= new ArrayList<>();
        medicineHistoryList.add(medicineHistory);
        Optional<UserMedicines> userMedicines= Optional.empty();
        when(userMedicineRepository.findById(12345)).thenThrow(JDBCConnectionException.class);
        try {
            userServiceimpl.sendUserMedicines(12345);
        } catch ( UserExceptionMessage e) {
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,e.getMessage());
        }
    }
    @Test
    @DisplayName("Save user Test")
    void saveUserTest() throws UserExceptionMessage {

        User user = new User("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),null,null);
        UserResponse userResponse= new UserResponse("Failed","User is already present",new ArrayList<>(Arrays.asList(user)),null,null);
        when(userServiceimpl.getUserByEmail(user.getEmail())).thenReturn(null);
        UserEntityDTO userEntityDTO= new UserEntityDTO("vinay","vinay@gmail.com");
        when(mapper.map(userEntityDTO, User.class)).thenReturn(user);
        when(util.generateToken(user.getUserName())).thenReturn("fiasfiugaojfbjkabfk");
        when(userRepository.save(user)).thenReturn(user);
        UserResponse userResponse1= userServiceimpl.saveUser(userEntityDTO,"eafyigfiagf","sfhoshgouahgo");
        Assertions.assertEquals(userResponse.getUserEntity().get(0).getUserName(),userResponse1.getUserEntity().get(0).getUserName());

    }

    @Test
    @DisplayName("Save user Test1")
    void saveUserTest1() throws UserExceptionMessage {

        User user = new User("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),null,null);
        UserResponse userResponse= new UserResponse("Failed","User is already present",new ArrayList<>(Arrays.asList(user)),null,null);
        UserEntityDTO userEntityDTO= new UserEntityDTO("vinay","vinay@gmail.com");
        when(userServiceimpl.getUserByEmail(user.getEmail())).thenReturn(user);
        UserResponse userResponse1= userServiceimpl.saveUser(userEntityDTO,"eafyigfiagf","sfhoshgouahgo");
        Assertions.assertEquals(userResponse.getUserEntity().get(0).getUserName(),userResponse1.getUserEntity().get(0).getUserName());
    }
    @Test
    @DisplayName("Save user Exception Test")
    void saveUserInnerExcpetion() throws  UserExceptionMessage {
        User user = new User("73578dfd-e7c9-4381-a348-113e72d80fa2",null,"vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),null,null);
        UserEntityDTO userEntityDTO= new UserEntityDTO("vinay","vinay@gmail.com");
        when(userServiceimpl.getUserByEmail(any())).thenReturn(null);
        when(mapper.map(userEntityDTO, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        try{
            userServiceimpl.saveUser(userEntityDTO,"eafyigfiagf","sfhoshgouahgo");
        }catch(UserExceptionMessage userExceptionMessage){
            Assertions.assertEquals("Error try again",userExceptionMessage.getMessage());
        }
    }
    @Test
    @DisplayName("Save user SqlTest")
    void saveUserSqlExcpetion() throws  UserExceptionMessage {
        User user = new User("73578dfd-e7c9-4381-a348-113e72d80fa2",null,"vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),null,null);
        UserEntityDTO userEntityDTO= new UserEntityDTO("vinay","vinay@gmail.com");
        when(userServiceimpl.getUserByEmail(any())).thenReturn(null);
        when(mapper.map(userEntityDTO, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenThrow(JDBCConnectionException.class);
        try{
            userServiceimpl.saveUser(userEntityDTO,"eafyigfiagf","sfhoshgouahgo");
        }catch(UserExceptionMessage userExceptionMessage){
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,userExceptionMessage.getMessage());
        }
    }
    @Test
    @DisplayName("Login Test")
    void loginTest() throws UserExceptionMessage {
        String fcmToken = "";
        UserDetails userDetails= new UserDetails();
        User userEntity = new User("73578dfd-e7c9-4381-a348-113e72d80fa2","Nikunj123","nikunj123@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),userDetails,null);
        when(userServiceimpl.getUserByEmail(any())).thenReturn(userEntity);
        userDetails.setFcmToken("afyuauvfiualfviuaofga");
        when(userDetailsRepository.save(userDetails)).thenReturn(userDetails);
        String jwtToken= util.generateToken(userEntity.getUserName());
        String refreshToken = passwordEncoder.encode(userEntity.getUserId());
        UserResponse userResponse= new UserResponse("Success", "Logged In",new ArrayList<>(Arrays.asList(userEntity)),jwtToken,refreshToken);
        UserResponse userResponse1= userServiceimpl.login("nikunj123@gmail.com","afyuauvfiualfviuaofga");
        Assertions.assertEquals(userResponse.getUserEntity().get(0).getUserId(),userResponse1.getUserEntity().get(0).getUserId());
    }

    @Test
    @DisplayName("Login exception Test")
    void loginExceptionTest() throws  UserExceptionMessage {
        String fcmToken = "";
        UserDetails userDetails= new UserDetails();
        User userEntity = new User("73578dfd-e7c9-4381-a348-113e72d80fa2",null,"nikunj123@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),userDetails,null);
        when(userServiceimpl.getUserByEmail(any())).thenReturn(userEntity);
        userDetails.setFcmToken("afyuauvfiualfviuaofga");
        when(userDetailsRepository.save(userDetails)).thenReturn(userDetails);
        String jwtToken= util.generateToken(userEntity.getUserName());
        String refreshToken = passwordEncoder.encode(userEntity.getUserId());
        try{
            userServiceimpl.login(userEntity.getEmail(),userEntity.getUserDetails().getFcmToken());
        }catch (UserExceptionMessage userExceptionMessage){
            Assertions.assertEquals("Data not found",userExceptionMessage.getMessage());
        }
    }
    @Test
    @DisplayName("Login exception SqlTest")
    void loginSqlExceptionTest() throws  UserExceptionMessage {
        String fcmToken = "";
        UserDetails userDetails= new UserDetails();
        User userEntity = new User("73578dfd-e7c9-4381-a348-113e72d80fa2",null,"nikunj123@gmail.com", LocalDateTime.now(), LocalDateTime.now(),LocalDateTime.now(),userDetails,null);
        when(userServiceimpl.getUserByEmail(any())).thenReturn(userEntity);
        userDetails.setFcmToken("afyuauvfiualfviuaofga");
        when(userDetailsRepository.save(userDetails)).thenThrow(JDBCConnectionException.class);
        String jwtToken= util.generateToken(userEntity.getUserName());
        String refreshToken = passwordEncoder.encode(userEntity.getUserId());
        try{
            userServiceimpl.login(userEntity.getEmail(),userEntity.getUserDetails().getFcmToken());
        }catch (UserExceptionMessage userExceptionMessage){
            Assertions.assertEquals(Constants.SQL_ERROR_MSG,userExceptionMessage.getMessage());
        }
    }
}





