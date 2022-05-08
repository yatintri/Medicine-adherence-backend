package com.example.user_service.service;

import com.example.user_service.exception.UserCaretakerException;
import com.example.user_service.model.UserCaretaker;
import com.example.user_service.pojos.dto.UserCaretakerDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CareTakerService {

     UserCaretaker saveCareTaker(UserCaretakerDTO userCaretakerDTO);

     UserCaretaker updateCaretakerStatus(String cId) throws UserCaretakerException;

     List<UserCaretaker> getPatientsUnderMe(String userId)throws UserCaretakerException;

     List<UserCaretaker> getPatientRequests(String userId) throws UserCaretakerException;

     List<UserCaretaker> getMyCaretakers(String userId) throws UserCaretakerException;

     List<UserCaretaker> getCaretakerRequestStatus(String userId);

     List<UserCaretaker> getCaretakerRequestsP(String userId) throws UserCaretakerException;

     Boolean delPatientReq(String cId);

    boolean sendImageToCaretaker(MultipartFile multipartFile , String filename , String caretakerId , String medName, Integer medId) throws IOException , UserCaretakerException;
}
//