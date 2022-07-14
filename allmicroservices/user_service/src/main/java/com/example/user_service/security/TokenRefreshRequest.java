package com.example.user_service.security;

import javax.validation.constraints.NotBlank;
/**
 * @Deprecated
 */
public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;
    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
