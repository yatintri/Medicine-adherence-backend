package com.example.user_service.exception;

/**
 *  Sends exception message for User
 */
public class UserExceptionMessage extends RuntimeException{

     public UserExceptionMessage(String errorMessage){
         super(errorMessage);
     }

}
