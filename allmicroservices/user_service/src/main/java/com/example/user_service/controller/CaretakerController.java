package com.example.user_service.controller;

import com.example.user_service.exception.UserCaretakerException;
import com.example.user_service.model.UserCaretaker;
import com.example.user_service.pojos.Notificationmessage;
import com.example.user_service.service.CareTakerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    @Autowired
    RabbitTemplate rabbitTemplate;
    // save caretaker for a patients
    @PostMapping(value = "/savecaretaker" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveCaretaker(@RequestBody UserCaretaker userCaretaker){

        return new ResponseEntity<>(careTakerService.saveCareTaker(userCaretaker), HttpStatus.CREATED);

    }

    // update request status if request is accepted or rejected
    @PutMapping(value = "/updatestatus")
    public ResponseEntity<?> updatecaretakerStatus(@RequestParam(name = "c_id") String c_id)
             throws UserCaretakerException {

        return  new ResponseEntity<>(careTakerService.updateCaretakerStatus(c_id), HttpStatus.OK);

    }

    // get record with a particular id
    @GetMapping(value = "/get_c_id")
    public ResponseEntity<?> getPatientCaretakerMap(){

        return new ResponseEntity(careTakerService.getPatientCaretakerMap(),HttpStatus.OK);

    }

    // fetch all the patients of a particular caretaker
    @GetMapping(value = "/myPatients(Caretaker)")
    public ResponseEntity<?> getPatientsUnderMe(@RequestParam(name = "caretaker_id") String  user_id){

        return new ResponseEntity(careTakerService.getPatientsUnderMe(user_id),HttpStatus.OK);
    }

    // fetch all the request sent by a patients to a caretaker
    @GetMapping(value = "/patientRequests(Caretaker)")
    public ResponseEntity<?> getPatientRequests(@RequestParam(name = "caretaker_id") String  user_id){

        return new ResponseEntity(careTakerService.getPatientRequests(user_id),HttpStatus.OK);

    }

    // where the patients can view all his caretakers
    @GetMapping(value = "/myCareTakers(Patient)")
    public ResponseEntity<?> getMyCaretakers(@RequestParam(name = "patient_id") String  user_id){

        return new ResponseEntity(careTakerService.getMyCaretakers(user_id),HttpStatus.OK);
    }

    // to fetch the caretaker request to a patients
    @GetMapping(value = "/caretakerRequests(sentstatus)")
    public ResponseEntity<?> getCaretakerSentStatus(@RequestParam(name = "patient_id") String  user_id){

        return new ResponseEntity(careTakerService.getCaretakerRequestStatus(user_id),HttpStatus.OK);

    }

    // to check the status of a request by patient
    @GetMapping(value = "/patientRequests(sentstatus)")
    public ResponseEntity<?> getPatientSentStatus(@RequestParam(name = "caretaker_id") String  user_id){

        return new ResponseEntity(careTakerService.getPatientRequestStatus(user_id),HttpStatus.OK);

    }

    // to check the status of a request by caretaker

    @GetMapping(value = "/caretakerRequests(for patient)")
    public ResponseEntity<?> getCaretakerRequestsP(@RequestParam(name = "patient_id") String  user_id){

        return new ResponseEntity(careTakerService.getCaretakerRequestsP(user_id),HttpStatus.OK);

    }

    @GetMapping(value = "/deletePatientRequest")
    public ResponseEntity<?> delPatientReq(@RequestParam(name = "c_id") String c_id){
        boolean b = careTakerService.delPatientReq(c_id);

        return new ResponseEntity(b,HttpStatus.OK);
    }

    @GetMapping(value = "/notifyuser")
    public ResponseEntity<?> notifyuserformed(@RequestParam(name = "fcm_token") String fcm_token){

        rabbitTemplate.convertAndSend("project_exchange","notification_key",new Notificationmessage(fcm_token,"Take medicine"));
        return new ResponseEntity<>("Ok",HttpStatus.OK);

    }

//
}
