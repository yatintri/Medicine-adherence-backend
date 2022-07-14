package com.example.user_service.service.caretaker;

import com.example.user_service.exception.UserCaretakerException;
import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.pojos.dto.UserCaretakerDTO;
import com.example.user_service.pojos.response.caretaker.CaretakerResponse;
import com.example.user_service.pojos.response.caretaker.CaretakerResponse1;
import com.example.user_service.pojos.response.caretaker.CaretakerResponsePage;
import com.example.user_service.pojos.response.image.SendImageResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

/**
 * This is an interface for caretaker service
 */
public interface CareTakerService {


    CaretakerResponse saveCareTaker(@Valid UserCaretakerDTO userCaretakerDTO) throws UserCaretakerException, UserExceptions, UserExceptionMessage;

    CaretakerResponse updateCaretakerStatus(String cId) throws UserCaretakerException, UserExceptions;

     CaretakerResponsePage getPatientsUnderMe(String userId, int page, int limit)throws UserCaretakerException, UserExceptions;

     CaretakerResponsePage getPatientRequests(String userId,int page,int limit) throws UserCaretakerException, UserExceptions;

     CaretakerResponsePage getMyCaretakers(String userId, int page, int limit) throws UserCaretakerException, UserExceptions;

     CaretakerResponse1 getCaretakerRequestStatus(String userId) throws UserCaretakerException;

     CaretakerResponsePage getCaretakerRequestsP(String userId, int page, int limit) throws UserCaretakerException, UserExceptions;

     String delPatientReq(String cId) throws UserExceptionMessage, UserCaretakerException, UserExceptions;

    SendImageResponse sendImageToCaretaker(MultipartFile multipartFile , String filename , String medName, String caretakerId , Integer medId) throws IOException , UserCaretakerException, UserExceptions;
}
