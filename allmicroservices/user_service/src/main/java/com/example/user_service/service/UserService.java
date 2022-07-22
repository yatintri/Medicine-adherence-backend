package com.example.user_service.service;

import com.example.user_service.model.User;
import com.example.user_service.pojos.request.UserEntityDTO;
import com.example.user_service.pojos.response.user.MailResponse;
import com.example.user_service.pojos.response.user.RefreshTokenResponse;
import com.example.user_service.pojos.response.user.UserProfileResponse;
import com.example.user_service.pojos.response.user.UserResponse;
import com.example.user_service.pojos.response.user.UserResponsePage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * This is an interface for user service
 */
public interface UserService {

     UserResponse saveUser(UserEntityDTO userEntityDTO, String fcmToken, String picPath);

     UserResponsePage getUsers(int page, int limit) ;

     UserProfileResponse getUserMedicine(String userId);

     User getUserById(String userId) ;

     List<User> getUserByName(String userName);

     MailResponse getUserByEmail(String email,String sender) ;

     String sendUserMedicines(Integer userId);

     UserResponse login(String mail , String fcmToken) ;

     RefreshTokenResponse getRefreshToken(HttpServletRequest token, String userId) ;



}
