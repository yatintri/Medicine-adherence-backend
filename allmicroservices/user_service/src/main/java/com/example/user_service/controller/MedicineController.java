package com.example.user_service.controller;


import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.*;
import com.example.user_service.pojos.dto.Medicinepojo;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserMedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.concurrent.ExecutionException;
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
    @PostMapping(value = "/medicine" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUserMedicine(@RequestParam("userId") String id, @RequestBody Medicinepojo medicinepojo) throws UserMedicineException, UserexceptionMessage {

        return new ResponseEntity<>(userMedicineService.saveUserMedicine(id,medicinepojo), HttpStatus.CREATED);

    }

    @PutMapping(value = "/medicine/status" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMedicineStatus(@RequestParam("medId") Integer medId) throws UserMedicineException {

        return new ResponseEntity<>(userMedicineService.updateMedicineStatus(medId),HttpStatus.CREATED);

    }

    @GetMapping(value = "/medicine", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserMedicines(@RequestParam("userId") String userId) throws InterruptedException, UserMedicineException, UserexceptionMessage, ExecutionException {


        return new ResponseEntity<>(userMedicineService.getallUserMedicines(userId).get(), HttpStatus.OK);

    }

    @PutMapping(value = "/medicineDetails" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editMedicineDetails(@RequestParam("medId") Integer id,
                                               @RequestBody Medicinepojo medicinepojo) throws UserMedicineException, UserexceptionMessage {

        return new ResponseEntity<>(userMedicineService.editMedicineDetails(id,medicinepojo),HttpStatus.CREATED);

    }

    @PostMapping(value = "/medicines/sync",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> syncdata(@RequestParam("userId") String userId , @RequestBody List<Medicinepojo> medicinepojo){

       UserEntity userEntity = userRepository.getuserbyid(userId);

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
       return new ResponseEntity<>("",HttpStatus.OK);


    }

    @PostMapping(value = "/medicinehistory/sync")
    public ResponseEntity<?> syncmedicinehistory(@RequestParam(name = "medId") Integer medId){

       UserMedicines userMedicines =  userMedicineRepository.getmedbyid(medId);
        System.out.println(userMedicines.getMedicineName());
      return null;

    }



}
///