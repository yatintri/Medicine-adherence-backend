package com.example.user_service.controller;

import com.example.user_service.exception.UserCaretakerException;

import com.example.user_service.model.UserCaretaker;
import com.example.user_service.pojos.Notificationmessage;
import com.example.user_service.pojos.dto.UserCaretakerDTO;
import com.example.user_service.service.CareTakerService;
import com.example.user_service.service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
public class CaretakerController {

    @Autowired
    private CareTakerService careTakerService;
    @Autowired
    private UserService userService;

    @Autowired
    RabbitTemplate rabbitTemplate;
    // save caretaker for a patients
    @PostMapping(value = "/request" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserCaretaker> saveCaretaker(@RequestBody UserCaretakerDTO userCaretakerDTO){

        return new ResponseEntity<>(careTakerService.saveCareTaker(userCaretakerDTO), HttpStatus.CREATED);

    }

    // update request status if request is accepted or rejected
    @PutMapping(value = "/accept")
    public ResponseEntity<UserCaretaker> updatecaretakerStatus(@RequestParam(name = "cId") String cId)
            throws UserCaretakerException {

        return  new ResponseEntity<>(careTakerService.updateCaretakerStatus(cId), HttpStatus.OK);

    }



    // fetch all the patients of a particular caretaker
    @GetMapping(value = "/patients")
    public ResponseEntity<List<UserCaretaker>> getPatientsUnderMe(@RequestParam(name = "caretakerId") String  userId){

        return new ResponseEntity<>(careTakerService.getPatientsUnderMe(userId),HttpStatus.OK);
    }

    // fetch all the request sent by a patients to a caretaker
    @GetMapping(value = "/patient/requests")
    public ResponseEntity<List<UserCaretaker>> getPatientRequests(@RequestParam(name = "caretakerId") String  userId){

        return new ResponseEntity<>(careTakerService.getPatientRequests(userId),HttpStatus.OK);

    }

    // where the patients can view all his caretakers
    @GetMapping(value = "/caretakers")
    public ResponseEntity<List<UserCaretaker>> getMyCaretakers(@RequestParam(name = "patientId") String  userId){

        return new ResponseEntity<>(careTakerService.getMyCaretakers(userId),HttpStatus.OK);
    }

    // to check the status of a request by caretaker

    @GetMapping(value = "/caretaker/requests")
    public ResponseEntity<List<UserCaretaker>> getCaretakerRequestsP(@RequestParam(name = "patientId") String  userId){

        return new ResponseEntity<>(careTakerService.getCaretakerRequestsP(userId),HttpStatus.OK);

    }

    @GetMapping(value = "/delete")
    public ResponseEntity<Boolean> delPatientReq(@RequestParam(name = "cId") String cId){
        boolean b = careTakerService.delPatientReq(cId);

        return new ResponseEntity<>(b,HttpStatus.OK);
    }

    @GetMapping(value = "/notifyuser")
    public ResponseEntity<String> notifyuserformed(@RequestParam(name = "fcmToken") String fcmToken , @RequestParam("medname") String body){

        rabbitTemplate.convertAndSend("project_exchange","notification_key",new Notificationmessage(fcmToken,"Take medicine","patient",body,""));
        return new ResponseEntity<>("Ok",HttpStatus.OK);

    }

    @PostMapping(value = "/image")
    public ResponseEntity<String> sendimagetocaretaker(@RequestParam(name = "image") MultipartFile multipartFile
            , @RequestParam(name = "name") String filename ,
                                                       @RequestParam(name = "id") String caretakerId) throws IOException, UserCaretakerException {

        careTakerService.sendimagetocaretaker(multipartFile , filename , caretakerId);
        return  new ResponseEntity<>("Ok",HttpStatus.OK);

    }

///
}