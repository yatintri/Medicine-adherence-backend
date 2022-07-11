package com.example.user_service.controllerTest;

import com.example.user_service.controller.UserController;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.model.UserEntity;
import com.example.user_service.pojos.dto.DetailsDTO;
import com.example.user_service.pojos.dto.LoginDTO;
import com.example.user_service.pojos.dto.MedicinePojo;
import com.example.user_service.pojos.dto.UserEntityDTO;
import com.example.user_service.pojos.response.user.UserDetailResponse;
import com.example.user_service.pojos.response.user.UserProfileResponse;
import com.example.user_service.pojos.response.user.UserResponse;
import com.example.user_service.pojos.response.user.UserResponsePage;
import com.example.user_service.service.user.UserService;
import com.example.user_service.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
class UserControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper=new ObjectMapper();

    @InjectMocks
    private UserController userController;

    @Mock
    UserService userService;

    @Mock
    RabbitTemplate rabbitTemplate;
    @Mock
    JwtUtil jwtUtil;

    @BeforeEach
    public void setUp(){
        this.mockMvc= MockMvcBuilders.standaloneSetup(userController).build();
    }
    UserEntity userEntity = new UserEntity("73578dfd-e7c9-4381-a348-113e72d80fa2","vinay","vinay@gmail.com", LocalDateTime.now(), LocalDateTime.now(),null,null);
    UserEntityDTO userEntityDTO= new UserEntityDTO("vinay","vinay@gmail.com");
    UserResponse userResponse= new UserResponse();
    DetailsDTO userDetailEntityDTO = new DetailsDTO("nikunj","vinay@gmail.com",LocalDateTime.now(),LocalDateTime.now(),"something",21,null,"Male","AB+","UnMarried",60);
    MedicinePojo userMedicineDTO= new MedicinePojo();
    List<MedicinePojo> userMedicineDTOList= new ArrayList<>();
    UserProfileResponse userProfileResponse= new UserProfileResponse();
    UserDetailResponse userDetailResponsePage= new UserDetailResponse();

    UserResponsePage userResponsePage= new UserResponsePage();


    @Test
    @DisplayName("Save user Test")
    @ExtendWith(MockitoExtension.class)
    void saveUser() throws Exception, UserExceptions {
        Mockito.when(userService.saveUser(any(),anyString(),anyString())).thenReturn(userResponse);
        String jsonRequest=objectMapper.writeValueAsString(userEntityDTO);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/user?fcmToken=yfdyiuwafviy&picPath=gedfigiagf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());
    }

//    @Test
//    @ExtendWith(MockitoExtension.class)
//    void refreshToken() throws Exception, UserExceptions {
//        Mockito.when(userService.getUserById(anyString())).thenReturn(userEntity);
//        Mockito.when(jwtUtil.generateToken(anyString())).thenReturn("jshfjdhfksj");
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/api/v1/refreshToken?uid=3549759519459734975")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//    }

    @Test
    @DisplayName("Login Test")
    @ExtendWith(MockitoExtension.class)
    void login() throws Exception, UserExceptions {
        LoginDTO loginDTO= new LoginDTO("hjLAJLJL","weirdn895@gmail.com");
        String jsonValue= objectMapper.writeValueAsString(loginDTO);
        Mockito.when(userService.login(loginDTO.getEmail(),loginDTO.getFcmToken())).thenReturn(userResponse);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonValue))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get users Test")
    @ExtendWith(MockitoExtension.class)
    void getUsers() throws Exception, UserExceptions {
        Mockito.when(userService.getUsers(anyInt(),anyInt())).thenReturn(CompletableFuture.completedFuture(userResponsePage));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users?page=0&limit=3").content(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get user by id Test")
    @ExtendWith(MockitoExtension.class)
    void getUserById() throws Exception, UserExceptions {
        Mockito.when(userService.getUserById(anyString())).thenReturn(userEntity);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/user?userId=69216495194519259")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get user by email Test")
    @ExtendWith(MockitoExtension.class)
    void getUserByEmail() throws Exception, UserExceptions {
        UserEntity userMailDTO= new UserEntity();
        Mockito.when(userService.getUserByEmail(anyString())).thenReturn(userMailDTO);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/email?email=vinay@gmail.com&sender=vinay")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get user by email exception Test")
    @ExtendWith(MockitoExtension.class)
    void getUserByEmailException() throws Exception, UserExceptions {
        UserEntity userMailDTO= new UserEntity();
        Mockito.when(userService.getUserByEmail(anyString())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/email?email=vinay@gmail.com&sender=vinay")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Send pdf Test")
    @ExtendWith(MockitoExtension.class)
    void sendPdf() throws Exception, UserExceptions {
        Mockito.when(userService.sendUserMedicines(123)).thenReturn("dydytkgjvhviyfoutyrxuljh");
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/pdf?medId=123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

    }

}
