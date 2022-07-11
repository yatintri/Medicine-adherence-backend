package com.example.user_service.pojos.response.user;

import com.example.user_service.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponsePage {

    private String status;
    private String message;
    private long totalItems;
    private int totalPage;
    private int currentPage;
    private List<UserEntity> userEntityStream;
}
