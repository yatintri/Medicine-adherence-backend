package com.example.user_service.controller;

import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.*;
import com.example.user_service.pojos.Medicinepojo;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserMedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/api/usermedicine")
public class MedicineController {

    @Autowired
    private UserMedicineService userMedicineService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMedicineRepository userMedicineRepository;

    // save caretaker for a patients
    @PostMapping(value = "/savemedicine/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUserMedicine(@PathVariable("userId") String id, @RequestBody UserMedicines userMedicines)throws UserMedicineException , UserexceptionMessage {

        return new ResponseEntity<>(userMedicineService.saveUserMedicine(id,userMedicines), HttpStatus.CREATED);

    }

    @PutMapping(value = "/updatestatus/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMedicineStatus(@PathVariable("medId") Integer medId)throws UserMedicineException{

        return new ResponseEntity<>(userMedicineService.updateMedicineStatus(medId),HttpStatus.CREATED);

    }

    @GetMapping(value = "/getusermedicine/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserMedicines(@PathVariable("userId") String userId) throws UserMedicineException,UserexceptionMessage,ExecutionException, InterruptedException {


        return new ResponseEntity<>(userMedicineService.getallUserMedicines(userId).get(), HttpStatus.OK);

    }

    @PutMapping(value = "/editmedicine/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editMedicineDetails(@PathVariable("medId") Integer id,
                                               @RequestBody UserMedicines userMedicines) throws UserMedicineException,UserexceptionMessage {

        return new ResponseEntity<>(userMedicineService.editMedicineDetails(id,userMedicines),HttpStatus.CREATED);

    }

    @PostMapping(value = "/syncmedicines",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> syncdata(@RequestParam("userId") String userId , @RequestBody List<Medicinepojo> medicinepojo){

       UserEntity userEntity = userRepository.getuserbyid(userId);
       List<UserMedicines> userMedicinesList = new ArrayList<>();
       for(Medicinepojo medicinepojo1 : medicinepojo) {

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
userMedicinesList.add(userMedicines);
       }

       userMedicineRepository.saveAll(userMedicinesList);


        // userMedicineService.syncdata(user_id,userMedicines);


       return new ResponseEntity<>("",HttpStatus.OK);


    }


}
//