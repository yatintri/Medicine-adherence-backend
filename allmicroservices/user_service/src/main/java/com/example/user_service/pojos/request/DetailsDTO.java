package com.example.user_service.pojos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * This is Pojo class for only Some Details used
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailsDTO {

    private String userName;
    private String email;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private String bio;
    private int age;
    private Long userContact;
    private String gender;
    private String bloodGroup;
    private String martialStatus;
    private int weight;

}
