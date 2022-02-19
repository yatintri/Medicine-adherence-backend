package com.example.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class UserExceptions {

 @ExceptionHandler({UserexceptionMessage.class})
 public ResponseEntity<?> getuserException(UserexceptionMessage uem , WebRequest webRequest){

     return new ResponseEntity<>(uem.getMessage() , HttpStatus.NOT_FOUND);

 }

}
