package com.example.user_service.service;

import com.example.user_service.exception.UserCaretakerException;
import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.model.UserCaretaker;
import com.example.user_service.pojos.dto.UserCaretakerDTO;
import com.example.user_service.pojos.response.CaretakerResponse;
import com.example.user_service.pojos.response.CaretakerResponse1;
import com.example.user_service.pojos.response.CaretakerResponsePage;
import com.example.user_service.pojos.response.SendImageResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CareTakerService {

     CaretakerResponse saveCareTaker(UserCaretakerDTO userCaretakerDTO, BindingResult bindingResult) throws UserCaretakerException, UserExceptions;

     CaretakerResponse updateCaretakerStatus(String cId,BindingResult bindingResult) throws UserCaretakerException, UserExceptions;

     CaretakerResponsePage getPatientsUnderMe(String userId, int page, int limit)throws UserCaretakerException, UserExceptions;

     CaretakerResponsePage getPatientRequests(String userId,int page,int limit) throws UserCaretakerException, UserExceptions;

     CaretakerResponsePage getMyCaretakers(String userId, int page, int limit) throws UserCaretakerException, UserExceptions;

     CaretakerResponse1 getCaretakerRequestStatus(String userId);

     CaretakerResponsePage getCaretakerRequestsP(String userId, int page, int limit) throws UserCaretakerException, UserExceptions;

     String delPatientReq(String cId) throws UserExceptionMessage, UserCaretakerException, UserExceptions;

    SendImageResponse sendImageToCaretaker(MultipartFile multipartFile , String filename , String caretakerId , String medName, Integer medId) throws IOException , UserCaretakerException, UserExceptions;
}
