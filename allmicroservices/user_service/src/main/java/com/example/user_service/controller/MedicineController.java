package com.example.user_service.controller;


import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.model.*;
import com.example.user_service.pojos.dto.MedicineHistoryDTO;

import com.example.user_service.pojos.dto.MedicinePojo;
import com.example.user_service.pojos.response.MedicineResponse;
import com.example.user_service.pojos.response.SyncResponse;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserMedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/")
public class MedicineController {

    @Autowired
    private UserMedicineService userMedicineService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMedicineRepository userMedicineRepository;

    // save caretaker for a patients
    private static final String MSG = "Success";


    @PostMapping(value = "/medicines/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SyncResponse> syncData(@RequestParam("userId") String userId, @RequestBody List<MedicinePojo> medicinePojo) throws UserMedicineException {

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
            return new ResponseEntity<>(new SyncResponse(MSG, "Synced Successfully"), HttpStatus.OK);
        }
        catch (Exception exception){
            throw new UserMedicineException("Sync failed");
        }

    }

    @PostMapping(value = "/medicine-history/sync")
    public ResponseEntity<SyncResponse> syncMedicineHistory(@RequestParam(name = "medId") Integer medId,
                                                 @RequestBody List<MedicineHistoryDTO> medicineHistory) throws UserMedicineException {
        try {

            userMedicineService.syncMedicineHistory(medId, medicineHistory);
            return new ResponseEntity<>(new SyncResponse(MSG, "Synced Successfully"), HttpStatus.OK);
        }catch (Exception e){
            throw new UserMedicineException("Sync failed");
        }

    }

    @GetMapping(value = "/medicine-histories")
    public ResponseEntity<MedicineResponse> getMedicineHistories(@RequestParam(name = "medId") Integer medId) throws UserMedicineException {

     return new ResponseEntity<>(userMedicineService.getMedicineHistory(medId),HttpStatus.OK);


    }

    @GetMapping(value = "/medicine-images")
    public ResponseEntity<List<Image>> getMedicineImages(@RequestParam(name = "medId") Integer medId){


        return new ResponseEntity<>(userMedicineService.getUserMedicineImages(medId),HttpStatus.OK);


    }


}
////