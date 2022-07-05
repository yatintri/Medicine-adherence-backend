package com.example.user_service.controller;

import com.example.user_service.exception.UserCaretakerException;

import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.pojos.Notificationmessage;
import com.example.user_service.pojos.dto.SendImageDto;
import com.example.user_service.pojos.dto.UserCaretakerDTO;
import com.example.user_service.pojos.response.*;
import com.example.user_service.service.CareTakerService;
import com.example.user_service.service.UserService;
import com.example.user_service.util.Messages;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Objects;

@RestController
@Validated
@RequestMapping(path = "/api/v1")
public class CaretakerController {
    

    private final CareTakerService careTakerService;

    private final UserService userService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${project.rabbitmq.exchange}")
    private String topicExchange;

    @Value("${project.rabbitmq.routingkey2}")
    private String routingKey2;

    public CaretakerController(CareTakerService careTakerService, UserService userService){
        this.careTakerService = careTakerService;
        this.userService = userService;
    }

    // save caretaker for a patients
    @Retryable(maxAttempts = 3)// retrying up to 3 times
    @PostMapping(value = "/request", produces = MediaType.APPLICATION_JSON_VALUE,consumes = "application/json")

    public ResponseEntity<CaretakerResponse> saveCaretaker(@Valid @RequestBody UserCaretakerDTO userCaretakerDTO, BindingResult bindingResult) throws UserCaretakerException , UserExceptions {
        if(bindingResult.hasErrors()){
          return  new ResponseEntity<>(new CaretakerResponse(Messages.VALIDATION, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(),null),HttpStatus.BAD_REQUEST);
        }
        CaretakerResponse userCaretaker = careTakerService.saveCareTaker(userCaretakerDTO,bindingResult);
        return new ResponseEntity<>(userCaretaker, HttpStatus.OK);

    }

    // update request status if request is accepted or rejected
    @Retryable(maxAttempts = 3)// retrying up to 3 times
    @PutMapping(value = "/accept",produces = MediaType.APPLICATION_JSON_VALUE,consumes = "application/json")
    public ResponseEntity<CaretakerResponse> updateCaretakerStatus(@NotNull @NotBlank @RequestParam(name = "cId") String cId,BindingResult bindingResult)
            throws UserCaretakerException, UserExceptions {
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new CaretakerResponse(Messages.VALIDATION, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(),null),HttpStatus.BAD_REQUEST);
        }
        CaretakerResponse userCaretaker = careTakerService.updateCaretakerStatus(cId,bindingResult);

        return new ResponseEntity<>(userCaretaker, HttpStatus.OK);

    }


    // fetch all the patients of a particular caretaker
    @Retryable(maxAttempts = 3)// retrying up to 3 times
    @GetMapping(value = "/patients",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CaretakerResponsePage> getPatientsUnderMe(@NotBlank @NotNull @RequestParam(name = "caretakerId") String userId,
                                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "limit", defaultValue = "30") int limit,BindingResult bindingResult)
            throws UserCaretakerException , UserExceptions{
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new CaretakerResponsePage(Messages.VALIDATION, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(),0,0,0,null),HttpStatus.BAD_REQUEST);
        }
        CaretakerResponsePage userCaretakerList = careTakerService.getPatientsUnderMe(userId,page,limit);
        return new ResponseEntity<>(userCaretakerList, HttpStatus.OK);
    }

    // fetch all the request sent by a patients to a caretaker
    @Retryable(maxAttempts = 3)// retrying up to 3 times
    @GetMapping(value = "/patient/requests",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CaretakerResponsePage> getPatientRequestsC(@NotNull @NotBlank @RequestParam(name = "caretakerId") String userId,
                                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                                  @RequestParam(value = "limit", defaultValue = "30") int limit,BindingResult bindingResult)
                                                                  throws UserCaretakerException, UserExceptions {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new CaretakerResponsePage(Messages.VALIDATION, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(),0,0,0,null),HttpStatus.BAD_REQUEST);
        }
        CaretakerResponsePage userCaretakerList = careTakerService.getPatientRequests(userId,page,limit);
        return new ResponseEntity<>(userCaretakerList, HttpStatus.OK);

    }

    // where the patients can view all his caretakers
    @Retryable(maxAttempts = 3)// retrying up to 3 times
    @GetMapping(value = "/caretakers",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CaretakerResponsePage> getMyCaretakers(@NotBlank @NotNull @RequestParam(name = "patientId") String userId,
                                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "limit", defaultValue = "30") int limit,BindingResult bindingResult) throws UserCaretakerException , UserExceptions{
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new CaretakerResponsePage(Messages.VALIDATION, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(),0,0,0,null),HttpStatus.BAD_REQUEST);
        }
        CaretakerResponsePage userCaretakerList = careTakerService.getMyCaretakers(userId,page,limit);
        return new ResponseEntity<>(userCaretakerList, HttpStatus.OK);
    }

    // to check the status of a request by caretaker
    @Retryable(maxAttempts = 3)// retrying up to 3 times
    @GetMapping(value = "/caretaker/requests",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CaretakerResponsePage> getCaretakerRequestsP(@NotNull @NotBlank @RequestParam(name = "patientId") String userId,
                                                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                                                    @RequestParam(value = "limit", defaultValue = "30") int limit,BindingResult bindingResult) throws UserCaretakerException , UserExceptions{
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new CaretakerResponsePage(Messages.VALIDATION, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(),0,0,0,null),HttpStatus.BAD_REQUEST);
        }
        CaretakerResponsePage userCaretakerList = careTakerService.getCaretakerRequestsP(userId,page,limit);
        return new ResponseEntity<>(userCaretakerList, HttpStatus.OK);

    }
    @Retryable(maxAttempts = 3)// retrying up to 3 times
    @GetMapping(value = "/delete",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CaretakerDelete> delPatientReq(@NotBlank @NotNull @RequestParam(name = "cId") String cId,BindingResult bindingResult)
            throws UserExceptionMessage, UserCaretakerException, UserExceptions {

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new CaretakerDelete(Messages.VALIDATION, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()),HttpStatus.BAD_REQUEST);
        }
        String delPatientStatus = careTakerService.delPatientReq(cId);
        CaretakerDelete caretakerDelete = new CaretakerDelete(delPatientStatus, Messages.DELETED_SUCCESS);
        return new ResponseEntity<>(caretakerDelete, HttpStatus.OK);
    }
    @Retryable(maxAttempts = 3)// retrying up to 3 times
    @GetMapping(value = "/notifyuser",produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(timeout = 4)
    public ResponseEntity<String> notifyUserForMed(@NotNull @NotBlank @RequestParam(name = "fcmToken") String fcmToken,@NotNull @NotBlank @RequestParam("medname") String body) {

        rabbitTemplate.convertAndSend(topicExchange, routingKey2, new Notificationmessage(fcmToken, "Take medicine", "patient", body, ""));
        return new ResponseEntity<>("Ok", HttpStatus.OK);

    }
    @Retryable(maxAttempts = 3)// retrying up to 3 times
    @PostMapping(value = "/image",produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(timeout = 10)
    public ResponseEntity<SendImageResponse> sendImageToCaretaker(@Valid @ModelAttribute SendImageDto sendImageDto,BindingResult bindingResult)
            throws IOException, UserCaretakerException , UserExceptions{

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new SendImageResponse(Messages.VALIDATION, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()),HttpStatus.BAD_REQUEST);
        }

        SendImageResponse sendImageResponse = careTakerService.sendImageToCaretaker(sendImageDto.getImage(),sendImageDto.getName(),sendImageDto.getMedName(),sendImageDto.getId(),sendImageDto.getMedId());
            return new ResponseEntity<>(sendImageResponse,HttpStatus.OK);

    }

}