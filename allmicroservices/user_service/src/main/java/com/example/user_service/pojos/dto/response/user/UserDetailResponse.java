package com.example.user_service.pojos.dto.response.user;


import com.example.user_service.model.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * This is a response class for User details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {

    private String status;

    private String message;

    private UserDetails userDetails;
}
