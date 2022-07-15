package com.example.user_service.pojos.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * This is a Pojo response class for JWT Authentication
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    @NotNull(message = "JWT is mandatory")
    @NotBlank(message = "JWT is mandatory")
    private String jwt;

}
