package com.example.user_service.controller;


import com.example.user_service.exception.UserExceptions;
import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.pojos.dto.MedicineHistoryDTO;

import com.example.user_service.pojos.dto.MedicinePojo;
import com.example.user_service.pojos.response.image.ImageListResponse;
import com.example.user_service.pojos.response.medicine.MedicineResponse;
import com.example.user_service.pojos.response.medicine.SyncResponse;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.usermedicine.UserMedicineService;
import com.example.user_service.util.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import java.util.Objects;

/**
 * This controller is used to create restful web services for user medicines
 */
@RestController
@Validated
@RequestMapping(path = "/api/v1/")
public class MedicineController {


    private final UserMedicineService userMedicineService;


    UserRepository userRepository;


    UserMedicineRepository userMedicineRepository;

    public MedicineController(UserMedicineService userMedicineService, UserRepository userRepository, UserMedicineRepository userMedicineRepository){
        this.userMedicineService = userMedicineService;
        this.userRepository = userRepository;
        this.userMedicineRepository = userMedicineRepository;
    }


    /**
     * Syncs local storage data of the application with the server
     */
    @Retryable(maxAttempts = 3)// retrying up to 3 times
    @PostMapping(value = "/medicines/sync", produces = MediaType.APPLICATION_JSON_VALUE, consumes = "application/json")
    public ResponseEntity<SyncResponse> syncData(@NotBlank @NotNull @RequestParam("userId") String userId, @Valid @RequestBody List<MedicinePojo> medicinePojo, BindingResult bindingResult)
            throws UserMedicineException , UserExceptions {

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new SyncResponse(Messages.VALIDATION, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(userMedicineService.syncData(userId,medicinePojo),HttpStatus.OK);

    }

    /**
     * Syncs medicine history of all the medicines from local storage to backend
     */
    @Retryable(maxAttempts = 3)// retrying up to 3 times
    @PostMapping(value = "/medicine-history/sync", produces = MediaType.APPLICATION_JSON_VALUE, consumes = "application/json")
    public ResponseEntity<SyncResponse> syncMedicineHistory(@NotNull @NotBlank @RequestParam(name = "medId") Integer medId,
                                                 @Valid @RequestBody List<MedicineHistoryDTO> medicineHistory,BindingResult bindingResult) throws UserMedicineException , UserExceptions{
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new SyncResponse(Messages.VALIDATION, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()),HttpStatus.BAD_REQUEST);
        }
        try {

            userMedicineService.syncMedicineHistory(medId, medicineHistory);
            return new ResponseEntity<>(new SyncResponse(Messages.SUCCESS, Messages.SYNC), HttpStatus.OK);
        }catch (Exception e){
            throw new UserMedicineException("Sync failed");
        }

    }
    /**
     * Fetch all medicines for a user by id
     */
    @Retryable(maxAttempts = 3)// retrying up to 3 times
    @GetMapping(value = "/medicine-histories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicineResponse> getMedicineHistories(@NotBlank @NotNull @RequestParam(name = "medId") Integer medId,
                                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "limit", defaultValue = "30") int limit) throws UserMedicineException, UserExceptions {

     return new ResponseEntity<>(userMedicineService.getMedicineHistory(medId,page,limit),HttpStatus.OK);


    }

    /**
     * Fetches all the images stored for that medicine
     */
    @Retryable(maxAttempts = 3)// retrying up to 3 times
    @GetMapping(value = "/medicine-images", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageListResponse> getMedicineImages(@NotBlank @NotNull @RequestParam(name = "medId") Integer medId, @RequestParam(value = "page", defaultValue = "0") int page,
                                                               @RequestParam(value = "limit", defaultValue = "30") int limit) throws UserExceptions, UserMedicineException {


        return new ResponseEntity<>(userMedicineService.getUserMedicineImages(medId,page,limit),HttpStatus.OK);


    }


}
