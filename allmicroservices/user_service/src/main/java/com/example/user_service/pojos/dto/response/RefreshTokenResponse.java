package com.example.user_service.pojos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is a response class for refresh Token
 * used to generate new jwt token
 *  */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenResponse {

    private String status;
    private String jwtToken;
    private String refreshToken;
}