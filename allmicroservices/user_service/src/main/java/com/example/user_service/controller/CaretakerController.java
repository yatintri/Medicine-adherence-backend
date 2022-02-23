package com.example.user_service.controller;

import com.example.user_service.model.UserCaretaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/caretaker")
public class CaretakerController {


    @PostMapping(value = "/savecaretaker" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveCaretaker(@RequestBody UserCaretaker userCaretaker){

        // save to data through service


        return new ResponseEntity<>(userCaretaker, HttpStatus.OK);

    }

    @PutMapping(value = "/updatestatus")
    public ResponseEntity<?> updatecaretakerStatus(@RequestParam(name = "c_id") String c_id){


        return  null;
    }




}
