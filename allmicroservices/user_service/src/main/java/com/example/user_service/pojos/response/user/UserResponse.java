package com.example.user_service.pojos.response.user;

import com.example.user_service.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
/**
 * This is a response class for User
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {


    private String status;
    private String message;
    private List<User> userEntity = new ArrayList<>();
    private String jwt;
    private String refreshToken;
}
