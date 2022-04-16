package com.example.user_service.exception;

import com.example.user_service.pojos.response.CaretakerResponse;
import com.example.user_service.pojos.response.MedicineResponse;
import com.example.user_service.pojos.response.UserResponse;
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
    private static final String ERROR="failed";
 @ExceptionHandler({UserexceptionMessage.class})
 public ResponseEntity<UserResponse> getuserException(UserexceptionMessage uem , WebRequest webRequest){
     UserResponse userResponse= new UserResponse(ERROR,uem.getMessage(),null,"");
     return new ResponseEntity<>(userResponse, HttpStatus.NOT_FOUND);

 }

    /**
     * Returns caretaker exception response
     */
 @ExceptionHandler({UserCaretakerException.class})
    public ResponseEntity<CaretakerResponse> getcaretakerexception(UserCaretakerException uce , WebRequest webRequest){
     CaretakerResponse caretakerResponse= new CaretakerResponse(ERROR,uce.getMessage(),null);
     return new ResponseEntity<>(caretakerResponse, HttpStatus.NOT_FOUND);

 }
    /**
     * Returns user medicine exception response
     */
    @ExceptionHandler({UserMedicineException.class})
    public ResponseEntity<MedicineResponse> getUserMedicineException(UserMedicineException udm , WebRequest webRequest){

        MedicineResponse medicineResponse= new MedicineResponse(ERROR, udm.getMessage(),null);
        return new ResponseEntity<>(medicineResponse, HttpStatus.NOT_FOUND);

    }



//

}
