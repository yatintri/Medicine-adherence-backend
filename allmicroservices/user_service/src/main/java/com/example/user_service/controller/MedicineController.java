package com.example.user_service.controller;

import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.*;
import com.example.user_service.service.UserMedicineService;
import org.hibernate.engine.spi.ManagedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/api/usermedicine")
public class MedicineController {

    @Autowired
    private UserMedicineService userMedicineService;

    // save caretaker for a patients
    @PostMapping(value = "/savemedicine/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUserMedicine(@PathVariable("id") String id, @RequestBody UserMedicines userMedicines)throws UserMedicineException , UserexceptionMessage {

        return new ResponseEntity<>(userMedicineService.saveUserMedicine(id,userMedicines), HttpStatus.CREATED);

    }

    @PutMapping(value = "/updatestatus/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMedicineStatus(@PathVariable("id") String med_id)throws UserMedicineException{

        return new ResponseEntity<>(userMedicineService.updateMedicineStatus(med_id),HttpStatus.CREATED);

    }

    @GetMapping(value = "/getusermedicine/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserMedicines(@PathVariable("id") String user_id) throws UserMedicineException,UserexceptionMessage,ExecutionException, InterruptedException {


        return new ResponseEntity<>(userMedicineService.getallUserMedicines(user_id).get(), HttpStatus.OK);

    }

    @PutMapping(value = "/editmedicine/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editMedicineDetails(@PathVariable("id") String id,
                                               @RequestBody UserMedicines userMedicines) throws UserMedicineException,UserexceptionMessage {

        return new ResponseEntity<>(userMedicineService.editMedicineDetails(id,userMedicines),HttpStatus.CREATED);

    }

}
