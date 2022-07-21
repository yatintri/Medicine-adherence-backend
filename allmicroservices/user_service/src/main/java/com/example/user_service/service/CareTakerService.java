package com.example.user_service.service;


import com.example.user_service.pojos.dto.request.UserCaretakerDTO;
import com.example.user_service.pojos.dto.response.caretaker.CaretakerDelete;
import com.example.user_service.pojos.dto.response.caretaker.CaretakerResponse;
import com.example.user_service.pojos.dto.response.caretaker.CaretakerResponse1;
import com.example.user_service.pojos.dto.response.caretaker.CaretakerResponsePage;
import com.example.user_service.pojos.dto.response.image.SendImageResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * This is an interface for caretaker service
 */
public interface CareTakerService {


    CaretakerResponse saveCareTaker(@Valid UserCaretakerDTO userCaretakerDTO) ;

    CaretakerResponse updateCaretakerStatus(String caretakerId) ;

     CaretakerResponsePage getPatientsUnderMe(String userId, int page, int limit);

     CaretakerResponsePage getPatientRequests(String userId,int page,int limit);

     CaretakerResponsePage getMyCaretakers(String userId, int page, int limit);

     CaretakerResponse1 getCaretakerRequestStatus(String userId);

     CaretakerResponsePage getCaretakerRequestsForPatient(String userId, int page, int limit) ;

     CaretakerDelete deletePatientRequest(String caretakerId) ;

    SendImageResponse sendImageToCaretaker(MultipartFile multipartFile , String filename , String medicineName, String caretakerId , Integer medicineId) ;
}
