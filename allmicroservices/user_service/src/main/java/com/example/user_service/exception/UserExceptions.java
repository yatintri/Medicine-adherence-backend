package com.example.user_service.exception;

import com.example.user_service.pojos.response.caretaker.CaretakerResponse;
import com.example.user_service.pojos.response.medicine.MedicineResponse;
import com.example.user_service.pojos.response.sql.SqlErrorResponse;
import com.example.user_service.pojos.response.user.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.Objects;


/**
 * This class is used to send error response
 */
@ControllerAdvice
public class UserExceptions extends Throwable {

    /**
     * Returns user exception response
     */
    private static final String ERROR = "failed";

    @ExceptionHandler({UserExceptionMessage.class})
    public ResponseEntity<UserResponse> getuserException(UserExceptionMessage uem, WebRequest webRequest) {
        UserResponse userResponse = new UserResponse(ERROR, uem.getMessage(), null, "", "");
        return new ResponseEntity<>(userResponse, HttpStatus.NOT_FOUND);

    }

    /**
     * Returns caretaker exception response
     */
    @ExceptionHandler({UserCaretakerException.class})
    public ResponseEntity<CaretakerResponse> getcaretakerexception(UserCaretakerException uce, WebRequest webRequest) {
        CaretakerResponse caretakerResponse = new CaretakerResponse(ERROR, uce.getMessage(), null);
        return new ResponseEntity<>(caretakerResponse, HttpStatus.NOT_FOUND);

    }

    /**
     * Returns image exception response
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<CaretakerResponse> imageException(ConstraintViolationException uce, WebRequest webRequest) {
        CaretakerResponse caretakerResponse = new CaretakerResponse(ERROR, uce.getMessage(), null);
        return new ResponseEntity<>(caretakerResponse, HttpStatus.NOT_FOUND);

    }

    /**
     * Returns exception response if caretaker is not valid
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<CaretakerResponse> getcaretakernotvalidexception(MethodArgumentNotValidException uce, WebRequest webRequest) {
        CaretakerResponse caretakerResponse = new CaretakerResponse(ERROR, Objects.requireNonNull(uce.getFieldError()).getDefaultMessage(), null);
        return new ResponseEntity<>(caretakerResponse, HttpStatus.NOT_FOUND);

    }

    /**
     * Returns user medicine exception response
     */
    @ExceptionHandler({UserMedicineException.class})
    public ResponseEntity<MedicineResponse> getUserMedicineException(UserMedicineException udm, WebRequest webRequest) {

        MedicineResponse medicineResponse = new MedicineResponse(ERROR, udm.getMessage(), null);
        return new ResponseEntity<>(medicineResponse, HttpStatus.NOT_FOUND);

    }

    /**
     * Returns Sql exception response
     */
    @ExceptionHandler({DataAccessExceptionMessage.class})
    public ResponseEntity<SqlErrorResponse> getSqlException(DataAccessExceptionMessage dae, WebRequest webRequest) {
        SqlErrorResponse sqlErrorResponse = new SqlErrorResponse(ERROR, dae.getMessage());
        return new ResponseEntity<>(sqlErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
