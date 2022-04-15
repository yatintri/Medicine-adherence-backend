package com.example.user_service.service;

import com.example.user_service.exception.UserCaretakerException;
import com.example.user_service.model.UserCaretaker;
import com.example.user_service.pojos.dto.UserCaretakerDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CareTakerService {

    public UserCaretaker saveCareTaker(UserCaretakerDTO userCaretakerDTO);

    public UserCaretaker updateCaretakerStatus(String cId) throws UserCaretakerException;

    public List<UserCaretaker> getPatientsUnderMe(String userId);

    public List<UserCaretaker> getPatientRequests(String userId);

    public List<UserCaretaker> getMyCaretakers(String userId);

    public List<UserCaretaker> getCaretakerRequestStatus(String userId);

    public List<UserCaretaker> getCaretakerRequestsP(String userId);

    public Boolean delPatientReq(String cId);

    boolean sendimagetocaretaker(MultipartFile multipartFile , String filename , String caretakerId) throws IOException , UserCaretakerException;
}
//