package com.example.user_service.controller;


import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserMedReminder;

import com.example.user_service.service.UserMedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/user/medreminder")
public class UserMedRemController {

    @Autowired
    private UserMedicineService userMedicineService;


    //something

//    @GetMapping("/getAll")
//    public ResponseEntity<List<UserMedReminder>> getMedReminders() throws ExecutionException, InterruptedException {
//        return new ResponseEntity(userMedicineService.getMedReminders(), HttpStatus.OK);
//    }

    @GetMapping("/getUserMedRem")
    public ResponseEntity<?> getMedRemById(@RequestParam Integer medicine_id)
            throws UserMedicineException, UserexceptionMessage {
        return  new ResponseEntity<>(userMedicineService.getMedRemById(medicine_id),HttpStatus.OK);
    }

    @PostMapping(value = "/saveMedReminder", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveMedReminders(@RequestBody UserMedReminder userMedReminder,
                                              @RequestParam Integer medicine_id)
                                              throws UserMedicineException, UserexceptionMessage{
        return  new ResponseEntity<>(userMedicineService.saveMedReminder(userMedReminder,medicine_id)
                , HttpStatus.OK);
    }

}
//