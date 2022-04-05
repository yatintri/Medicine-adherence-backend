package com.example.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;

/**
 * This class is used to send error response
 */
@ControllerAdvice
public class UserExceptions {

    /**
     * Returns user exception response
     */
 @ExceptionHandler({UserexceptionMessage.class})
 public ResponseEntity<?> getuserException(UserexceptionMessage uem , WebRequest webRequest){
     HashMap<String,String> map = new HashMap<>();
     map.put("error",uem.getMessage());
     return new ResponseEntity<>(map , HttpStatus.NOT_FOUND);

 }

    /**
     * Returns caretaker exception response
     */
 @ExceptionHandler({UserCaretakerException.class})
    public ResponseEntity<?> getcaretakerexception(UserCaretakerException uce , WebRequest webRequest){

     HashMap<String , String> map = new HashMap<>();
     map.put("error" , uce.getMessage());
     return new ResponseEntity<>(map , HttpStatus.BAD_REQUEST);

 }
    /**
     * Returns user medicine exception response
     */
    @ExceptionHandler({UserMedicineException.class})
    public ResponseEntity<?> getUserMedicineException(UserMedicineException udm , WebRequest webRequest){

        HashMap<String , String> map = new HashMap<>();
        map.put("error" , udm.getMessage());
        return new ResponseEntity<>(map , HttpStatus.BAD_REQUEST);

    }



//

}
