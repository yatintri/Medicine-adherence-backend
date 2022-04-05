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
    public ResponseEntity<?> saveUserMedicine(@PathVariable("user_id") String id, @RequestBody UserMedicines userMedicines)throws UserMedicineException , UserexceptionMessage {

        return new ResponseEntity<>(userMedicineService.saveUserMedicine(id,userMedicines), HttpStatus.CREATED);

    }

    @PutMapping(value = "/updatestatus/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMedicineStatus(@PathVariable("med_id") Integer med_id)throws UserMedicineException{

        return new ResponseEntity<>(userMedicineService.updateMedicineStatus(med_id),HttpStatus.CREATED);

    }

    @GetMapping(value = "/getusermedicine/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserMedicines(@PathVariable("user_id") String user_id) throws UserMedicineException,UserexceptionMessage,ExecutionException, InterruptedException {


        return new ResponseEntity<>(userMedicineService.getallUserMedicines(user_id).get(), HttpStatus.OK);

    }

    @PutMapping(value = "/editmedicine/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editMedicineDetails(@PathVariable("med_id") Integer id,
                                               @RequestBody UserMedicines userMedicines) throws UserMedicineException,UserexceptionMessage {

        return new ResponseEntity<>(userMedicineService.editMedicineDetails(id,userMedicines),HttpStatus.CREATED);

    }

    @PostMapping(value = "/syncmedicines",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> syncdata(@RequestParam("user_id") String user_id , @RequestBody List<Medicinepojo> medicinepojo){

       UserEntity userEntity = userRepository.getuserbyid(user_id);
       List<UserMedicines> userMedicinesList = new ArrayList<>();
       for(Medicinepojo medicinepojo1 : medicinepojo) {

       UserMedicines userMedicines = new UserMedicines();

       userMedicines.setMedicine_des(medicinepojo1.getMedicine_des());
       userMedicines.setMedicine_name(medicinepojo1.getMedicine_name());
       userMedicines.setDays(medicinepojo1.getDays());
       userMedicines.setMedicine_id(medicinepojo1.getUser_id());
       userMedicines.setEnd_date(medicinepojo1.getEnd_date());
       userMedicines.setTitle(medicinepojo1.getTitle());
       userMedicines.setCurrent_count(medicinepojo1.getCurrent_count());
       userMedicines.setTotal_med_reminders(medicinepojo1.getTotal_med_reminders());
       userMedicines.setStart_date(medicinepojo1.getStart_date());
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