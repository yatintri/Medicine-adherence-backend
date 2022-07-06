package com.example.user_service.pojos.response;

import com.example.user_service.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {


    private String status;
    private String message;
    private List< UserEntity> userEntity = new ArrayList<>();
    private String jwt;
    private String refreshToken;
}
