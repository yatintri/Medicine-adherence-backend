package com.example.user_service.controller;


import com.example.user_service.model.UserMedReminder;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.service.UserMedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/api/user/medreminder")
public class UserMedRemController {

    @Autowired
    private UserMedicineService userMedicineService;


    //something

    @GetMapping("/getAll")
    public ResponseEntity<List<UserMedReminder>> getMedReminders() throws ExecutionException, InterruptedException {
        return new ResponseEntity(userMedicineService.getMedReminders(), HttpStatus.OK);
    }

    @GetMapping("/getUserMedRem")
    public ResponseEntity<?> getMedRemById(@RequestParam String medicine_id){
        return  new ResponseEntity<>(userMedicineService.getMedRemById(medicine_id),HttpStatus.OK);
    }

    @PostMapping(value = "/saveMedReminder", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveMedReminders(@RequestBody UserMedReminder userMedReminder){
        return  new ResponseEntity<>(userMedicineService.saveMedReminder(userMedReminder), HttpStatus.OK);
    }

}