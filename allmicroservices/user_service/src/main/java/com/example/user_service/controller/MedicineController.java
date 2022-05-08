package com.example.user_service.controller;


import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.model.*;
import com.example.user_service.pojos.dto.MedicineHistoryDTO;
import com.example.user_service.pojos.dto.Medicinepojo;
import com.example.user_service.pojos.response.MedicineResponse;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserMedicineService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping(value = "/medicines/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> syncData(@RequestParam("userId") String userId, @RequestBody List<Medicinepojo> medicinepojo) {

        UserEntity userEntity = userRepository.getUserById(userId);

        List<UserMedicines> userMedicinesList = medicinepojo.stream().map(medicinepojo1 -> {
                    UserMedicines userMedicines = new UserMedicines();

                    userMedicines.setMedicineDes(medicinepojo1.getMedicineDes());
                    userMedicines.setMedicineName(medicinepojo1.getMedicineName());
                    userMedicines.setDays(medicinepojo1.getDays());
                    userMedicines.setMedicineId(medicinepojo1.getUserId());
                    userMedicines.setEndDate(medicinepojo1.getEndDate());
                    userMedicines.setTitle(medicinepojo1.getTitle());
                    userMedicines.setCurrentCount(medicinepojo1.getCurrentCount());
                    userMedicines.setTotalMedReminders(medicinepojo1.getTotalMedReminders());
                    userMedicines.setStartDate(medicinepojo1.getStartDate());
                    userMedicines.setTime(medicinepojo1.getTime());
                    userMedicines.setUserEntity(userEntity);

                    return userMedicines;
                })
                .collect(Collectors.toList());

        userMedicineRepository.saveAll(userMedicinesList);
        return new ResponseEntity<>("", HttpStatus.OK);


    }

    @PostMapping(value = "/medicine-history/sync")
    public ResponseEntity<String> syncMedicineHistory(@RequestParam(name = "medId") Integer medId,
                                                 @RequestBody List<MedicineHistoryDTO> medicineHistory) throws UserMedicineException {

        userMedicineService.syncMedicineHistory(medId , medicineHistory);
        return new ResponseEntity<>("OK",HttpStatus.OK);

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