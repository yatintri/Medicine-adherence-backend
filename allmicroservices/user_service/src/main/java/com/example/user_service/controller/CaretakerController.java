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


        return  new ResponseEntity<>(careTakerService.updateCaretakerStatus(c_id), HttpStatus.OK);
    }

    @GetMapping(value = "/get_c_id")
    public ResponseEntity<?> getPatientCaretakerMap(){

        return new ResponseEntity(careTakerService.getPatientCaretakerMap(),HttpStatus.OK);
    }
    @GetMapping(value = "/myPatients(Caretaker)")
    public ResponseEntity<?> getPatientsUnderMe(@RequestParam(name = "caretaker_id") String  user_id){

        return new ResponseEntity(careTakerService.getPatientsUnderMe(user_id),HttpStatus.OK);
    }

    @GetMapping(value = "/patientRequests(Caretaker)")
    public ResponseEntity<?> getPatientRequests(@RequestParam(name = "caretaker_id") String  user_id){

        return new ResponseEntity(careTakerService.getPatientRequests(user_id),HttpStatus.OK);
    }

    @GetMapping(value = "/myCareTakers(Patient)")
    public ResponseEntity<?> getMyCaretakers(@RequestParam(name = "patient_id") String  user_id){

        return new ResponseEntity(careTakerService.getMyCaretakers(user_id),HttpStatus.OK);
    }

    @GetMapping(value = "/caretakerRequests(Patient)")
    public ResponseEntity<?> getCaretakerRequests(@RequestParam(name = "patient_id") String  user_id){

        return new ResponseEntity(careTakerService.getCaretakerRequests(user_id),HttpStatus.OK);
    }


}
