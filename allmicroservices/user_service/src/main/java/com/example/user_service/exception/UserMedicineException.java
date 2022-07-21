package com.example.user_service.exception;

/**
 * Sends exception message  for user medicines
 */
public class UserMedicineException extends RuntimeException {

    public UserMedicineException(String error){

        super(error);

    }
}
