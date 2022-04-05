package com.example.user_service.exception;

/**
 *  Sends exception message for User
 */
public class UserexceptionMessage extends Exception{

     public UserexceptionMessage(String errormessage){
         super(errormessage);
     }

}
