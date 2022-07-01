package com.example.user_service.pojos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


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
