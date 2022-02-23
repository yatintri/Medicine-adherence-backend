package com.example.user_service.controller;

import com.example.user_service.model.UserCaretaker;
import com.example.user_service.service.CareTakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/caretaker")
public class CaretakerController {

    @Autowired
    private CareTakerService careTakerService;

    @PostMapping(value = "/savecaretaker" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveCaretaker(@RequestBody UserCaretaker userCaretaker){

        return new ResponseEntity<>(careTakerService.saveCareTaker(userCaretaker), HttpStatus.CREATED);

    }

    @PutMapping(value = "/updatestatus")
    public ResponseEntity<?> updatecaretakerStatus(@RequestParam(name = "c_id") String c_id){


        return  new ResponseEntity<>(careTakerService.updatecaretakerStatus(c_id), HttpStatus.OK);
    }

    @GetMapping(value = "/getPatients")
    public ResponseEntity<?> getPatientsUnderMe(@RequestParam(name = "user_id") String  user_id){

        return new ResponseEntity(careTakerService.getPatientsUnderMe(user_id),HttpStatus.OK);
    }




}
