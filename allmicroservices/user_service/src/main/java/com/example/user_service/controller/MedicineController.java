package com.example.user_service.controller;


import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.model.*;
import com.example.user_service.pojos.dto.MedicineHistoryDTO;

import com.example.user_service.pojos.dto.MedicinePojo;
import com.example.user_service.pojos.response.*;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.CareTakerService;
import com.example.user_service.service.UserMedicineService;
import com.example.user_service.service.UserService;
import com.example.user_service.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import java.util.Objects;
import java.util.stream.Collectors;

@RestController
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


    // save caretaker for a patients
    @PostMapping(value = "/medicines/sync", produces = MediaType.APPLICATION_JSON_VALUE, consumes = "application/json")
    public ResponseEntity<SyncResponse> syncData(@NotBlank @NotNull @RequestParam("userId") String userId, @Valid @RequestBody List<MedicinePojo> medicinePojo, BindingResult bindingResult)
            throws UserMedicineException , UserExceptions {

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new SyncResponse(Messages.VALIDATION,bindingResult.getFieldError().getDefaultMessage()),HttpStatus.BAD_REQUEST);
        }

        try {
            UserEntity userEntity = userRepository.getUserById(userId);

            List<UserMedicines> userMedicinesList = medicinePojo.stream().map(medicinePojo1 -> {
                        UserMedicines userMedicines = new UserMedicines();

                        userMedicines.setMedicineDes(medicinePojo1.getMedicineDes());
                        userMedicines.setMedicineName(medicinePojo1.getMedicineName());
                        userMedicines.setDays(medicinePojo1.getDays());
                        userMedicines.setMedicineId(medicinePojo1.getUserId());
                        userMedicines.setEndDate(medicinePojo1.getEndDate());
                        userMedicines.setTitle(medicinePojo1.getTitle());
                        userMedicines.setCurrentCount(medicinePojo1.getCurrentCount());
                        userMedicines.setTotalMedReminders(medicinePojo1.getTotalMedReminders());
                        userMedicines.setStartDate(medicinePojo1.getStartDate());
                        userMedicines.setTime(medicinePojo1.getTime());
                        userMedicines.setUserEntity(userEntity);

                        return userMedicines;
                    })
                    .collect(Collectors.toList());

            userMedicineRepository.saveAll(userMedicinesList);
            return new ResponseEntity<>(new SyncResponse(Messages.SUCCESS, Messages.SYNC), HttpStatus.OK);
        }
        catch (Exception exception){
            exception.printStackTrace();
            throw new UserMedicineException("Sync failed");

        }

    }

    @PostMapping(value = "/medicine-history/sync", produces = MediaType.APPLICATION_JSON_VALUE, consumes = "application/json")
    public ResponseEntity<SyncResponse> syncMedicineHistory(@NotNull @NotBlank @RequestParam(name = "medId") Integer medId,
                                                 @Valid @RequestBody List<MedicineHistoryDTO> medicineHistory,BindingResult bindingResult) throws UserMedicineException , UserExceptions{
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new SyncResponse(Messages.VALIDATION,bindingResult.getFieldError().getDefaultMessage()),HttpStatus.BAD_REQUEST);
        }
        try {

            userMedicineService.syncMedicineHistory(medId, medicineHistory);
            return new ResponseEntity<>(new SyncResponse(Messages.SUCCESS, Messages.SYNC), HttpStatus.OK);
        }catch (Exception e){
            throw new UserMedicineException("Sync failed");
        }

    }

    @GetMapping(value = "/medicine-histories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicineResponse> getMedicineHistories(@NotBlank @NotNull @RequestParam(name = "medId") Integer medId,
                                                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                                                     @RequestParam(value = "limit", defaultValue = "30") int limit, BindingResult bindingResult) throws UserMedicineException, UserExceptions {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MedicineResponse(Messages.VALIDATION, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(),null),HttpStatus.BAD_REQUEST);
        }

     return new ResponseEntity<>(userMedicineService.getMedicineHistory(medId,page,limit),HttpStatus.OK);


    }

    @GetMapping(value = "/medicine-images", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageListResponse> getMedicineImages(@NotBlank @NotNull @RequestParam(name = "medId") Integer medId, @RequestParam(value = "page", defaultValue = "0") int page,
                                                                        @RequestParam(value = "limit", defaultValue = "30") int limit, BindingResult bindingResult) throws UserExceptions{

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new ImageListResponse(Messages.VALIDATION, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(),null),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userMedicineService.getUserMedicineImages(medId,page,limit),HttpStatus.OK);


    }


}
