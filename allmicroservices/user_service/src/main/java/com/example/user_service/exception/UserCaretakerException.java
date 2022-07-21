package com.example.user_service.exception;

/**
 * Sends exception message for caretaker
 */
public class UserCaretakerException extends RuntimeException{

    public UserCaretakerException(String error){

        super(error);

    }
}