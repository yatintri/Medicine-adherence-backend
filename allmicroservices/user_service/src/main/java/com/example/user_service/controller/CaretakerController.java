package com.example.user_service.controller;

import com.example.user_service.exception.UserCaretakerException;

import com.example.user_service.model.UserCaretaker;
import com.example.user_service.pojos.Notificationmessage;
import com.example.user_service.pojos.dto.UserCaretakerDTO;
import com.example.user_service.pojos.response.CaretakerDelete;
import com.example.user_service.pojos.response.CaretakerResponse;
import com.example.user_service.pojos.response.CaretakerResponse1;
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

    private static final String MSG="Success";
    private static final String MSG1="Data found";
    @Autowired
    private CareTakerService careTakerService;
    @Autowired
    private UserService userService;

    @Autowired
    RabbitTemplate rabbitTemplate;
    // save caretaker for a patients
    @PostMapping(value = "/request" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CaretakerResponse> saveCaretaker(@RequestBody UserCaretakerDTO userCaretakerDTO){
        UserCaretaker userCaretaker = careTakerService.saveCareTaker(userCaretakerDTO);
        CaretakerResponse caretakerResponse= new CaretakerResponse(MSG, "Request sent successfully", userCaretaker);
        return new ResponseEntity<>(caretakerResponse, HttpStatus.OK);

    }

    // update request status if request is accepted or rejected
    @PutMapping(value = "/accept")
    public ResponseEntity<CaretakerResponse> updatecaretakerStatus(@RequestParam(name = "cId") String cId)
            throws UserCaretakerException {
        UserCaretaker userCaretaker = careTakerService.updateCaretakerStatus(cId);
        CaretakerResponse caretakerResponse= new CaretakerResponse(MSG,"Status updated",userCaretaker);
        return  new ResponseEntity<>(caretakerResponse, HttpStatus.OK);

    }



    // fetch all the patients of a particular caretaker
    @GetMapping(value = "/patients")
    public ResponseEntity<CaretakerResponse1> getPatientsUnderMe(@RequestParam(name = "caretakerId") String  userId){
        List<UserCaretaker> userCaretakerList= careTakerService.getPatientsUnderMe(userId);
        CaretakerResponse1 caretakerResponse1= new CaretakerResponse1(MSG,MSG1,userCaretakerList);
        return new ResponseEntity<>(caretakerResponse1,HttpStatus.OK);
    }

    // fetch all the request sent by a patients to a caretaker
    @GetMapping(value = "/patient/requests")
    public ResponseEntity<CaretakerResponse1> getPatientRequestsC(@RequestParam(name = "caretakerId") String  userId){
        List<UserCaretaker> userCaretakerList= careTakerService.getPatientRequests(userId);
        CaretakerResponse1 caretakerResponse1= new CaretakerResponse1(MSG,MSG1,userCaretakerList);
        return new ResponseEntity<>(caretakerResponse1,HttpStatus.OK);

    }

    // where the patients can view all his caretakers
    @GetMapping(value = "/caretakers")
    public ResponseEntity<CaretakerResponse1> getMyCaretakers(@RequestParam(name = "patientId") String  userId){
        List<UserCaretaker> userCaretakerList= careTakerService.getMyCaretakers(userId);
        CaretakerResponse1 caretakerResponse1= new CaretakerResponse1(MSG,MSG1, userCaretakerList);
        return new ResponseEntity<>(caretakerResponse1,HttpStatus.OK);
    }

    // to check the status of a request by caretaker

    @GetMapping(value = "/caretaker/requests")
    public ResponseEntity<CaretakerResponse1> getCaretakerRequestsP(@RequestParam(name = "patientId") String  userId){
        List<UserCaretaker> userCaretakerList= careTakerService.getCaretakerRequestsP(userId);
        CaretakerResponse1 caretakerResponse1= new CaretakerResponse1(MSG,MSG1,userCaretakerList);
        return new ResponseEntity<>(caretakerResponse1,HttpStatus.OK);

    }

    @GetMapping(value = "/delete")
    public ResponseEntity<CaretakerDelete> delPatientReq(@RequestParam(name = "cId") String cId){
        boolean b = careTakerService.delPatientReq(cId);
        CaretakerDelete caretakerDelete= new CaretakerDelete(b,"Deleted successfully");
        return new ResponseEntity<>(caretakerDelete,HttpStatus.OK);
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