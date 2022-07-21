package com.example.user_service.controllerTest;

import com.example.user_service.controller.UserDetailController;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.model.UserDetails;
import com.example.user_service.pojos.dto.request.UserDetailsDTO;
import com.example.user_service.service.UserDetailService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

@RunWith(SpringJUnit4ClassRunner.class)
 class UserDetailTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper= new ObjectMapper();
    @InjectMocks
    private UserDetailController userDetailController;

    @Mock
    UserDetailService userDetailService;

    @BeforeEach
    public void setUp(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(userDetailController).build();
    }

    UserDetails userDetails = new UserDetails("73578dfd-e7c9-4381-a348-113e72d80fa2","something",null,21,null,414124,null,5L,56L,null,"male","AB+","Unmarried",60,2314,null, LocalDateTime.now(),LocalDateTime.now(),null);

    @Test
    @DisplayName("Update details Test")
    @ExtendWith(MockitoExtension.class)
    void updateUserDetails() throws Exception {
        UserDetailsDTO userDetailsDTO= new UserDetailsDTO("something",21,352L,"male","Ab+","Unmarried",66);
        String jsonTest = objectMapper.writeValueAsString(userDetailsDTO);
        Mockito.when(userDetailService.saveUserDetail("534851457147",userDetailsDTO)).thenReturn(userDetails);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/user-details?userId=534851457147")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonTest))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }



}
