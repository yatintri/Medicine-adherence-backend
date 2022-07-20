package com.example.user_service.pojos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * This is a Pojo class for User
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntityDTO {

    @NotBlank(message = "User name is mandatory")
    @NotNull(message = "User name is mandatory")
    private String userName;

    @NotBlank(message = "Email is mandatory")
    @NotNull(message = "Email is mandatory")
    @Email
    private String email;
}
