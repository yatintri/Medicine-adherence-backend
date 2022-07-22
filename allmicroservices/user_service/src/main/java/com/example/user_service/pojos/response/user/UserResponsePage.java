package com.example.user_service.pojos.response.user;

import com.example.user_service.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
/**
 * This is a response class for User to return total items, current page, total page
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponsePage implements Serializable {

    private String status;
    private String message;
    private long totalItems;
    private int totalPage;
    private int currentPage;
    private List<User> userEntityStream;
}
