package com.example.user_service.controller;


import java.util.Objects;

import com.example.user_service.pojos.NotificationMessage;
import com.example.user_service.util.Constants;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.user_service.exception.UserCaretakerException;
import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.pojos.request.SendImageDto;
import com.example.user_service.pojos.request.UserCaretakerDTO;
import com.example.user_service.pojos.response.caretaker.CaretakerDelete;
import com.example.user_service.pojos.response.caretaker.CaretakerResponse;
import com.example.user_service.pojos.response.caretaker.CaretakerResponsePage;
import com.example.user_service.pojos.response.image.SendImageResponse;
import com.example.user_service.service.CareTakerService;

import static com.example.user_service.util.Constants.DELETED_SUCCESS;
import static com.example.user_service.util.Constants.SUCCESS;

/**
 * This controller is used to create restful web services for caretaker
 */
@RestController
@Validated
@RequestMapping(path = "/api/v1")
public class CaretakerController {
    private final CareTakerService careTakerService;
    RabbitTemplate rabbitTemplate;
    @Value("${project.rabbitmq.exchange}")
    private String topicExchange;
    @Value("${project.rabbitmq.routingKeyNotification}")
    private String routingKeyNotification;

    private final Logger logger = LoggerFactory.getLogger(CaretakerController.class);


    public CaretakerController(CareTakerService careTakerService, RabbitTemplate rabbitTemplate) {
        this.careTakerService = careTakerService;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Saves caretaker for patients
     *
     * @param bindingResult Spring's object that holds the result of the validation and binding and contains errors that may have occurred
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "Saves caretaker for patients")
    @PostMapping(
            value = "/request",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CaretakerResponse> saveCaretaker(@Valid
                                                           @RequestBody UserCaretakerDTO userCaretakerDTO, BindingResult bindingResult)
            throws UserCaretakerException, UserExceptionMessage {

        logger.info("Saving Caretaker(s) : {}",userCaretakerDTO);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new CaretakerResponse(Constants.VALIDATION,
                    Objects.requireNonNull(
                            bindingResult.getFieldError()).getDefaultMessage(),
                    null),
                    HttpStatus.BAD_REQUEST);
        }

        CaretakerResponse userCaretaker = careTakerService.saveCareTaker(userCaretakerDTO);

        return new ResponseEntity<>(userCaretaker, HttpStatus.CREATED);
    }

    /**
     * Update request status if request is accepted or rejected
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "Update request status if request is accepted or rejected")
    @PutMapping(
            value = "/accept",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = "application/json"
    )
    public ResponseEntity<CaretakerResponse> updateCaretakerStatus(@NotNull
                                                                   @NotBlank
                                                                   @RequestParam(name = "caretakerId") String caretakerId) throws UserCaretakerException{
        logger.info("Updating Caretaker request status : {}",caretakerId);

        CaretakerResponse userCaretaker = careTakerService.updateCaretakerStatus(caretakerId);

        return new ResponseEntity<>(userCaretaker, HttpStatus.OK);
    }

    /**
     * To check the status of a request by caretaker
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "To check the status of a request by caretaker")
    @GetMapping(
            value = "/caretaker/requests",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CaretakerResponsePage> getCaretakerRequestsForPatients(@NotNull
                                                                       @NotBlank
                                                                       @RequestParam(name = "patientId") String userId, @RequestParam(value = "page") int page,
                                                                       @RequestParam(value = "limit") int limit)
                                                                       throws UserCaretakerException {
        logger.info("Fetching caretaker requests : {}",userId);

        CaretakerResponsePage userCaretakerList = careTakerService.getCaretakerRequestsForPatient(userId, page, limit);

        return new ResponseEntity<>(userCaretakerList, HttpStatus.OK);
    }

    /**
     * Fetch all the caretakers for a patient
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "Fetch all the caretakers for a patient")
    @GetMapping(
            value = "/caretakers",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CaretakerResponsePage> getMyCaretakers(@NotBlank
                                                                 @NotNull
                                                                 @RequestParam(name = "patientId") String userId, @RequestParam(value = "page") int page,
                                                                 @RequestParam(value = "limit") int limit)
            throws UserCaretakerException {

        logger.info("Fetching caretaker(s) : {}",userId);

        CaretakerResponsePage userCaretakerList = careTakerService.getMyCaretakers(userId, page, limit);

        return new ResponseEntity<>(userCaretakerList, HttpStatus.OK);
    }

    /**
     * Fetch all the request sent by patients to a caretaker
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "Fetch all the request sent by patients to a caretaker")
    @GetMapping(
            value = "/patient/requests",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CaretakerResponsePage> getPatientRequestForCaretaker(@NotNull
                                                                     @NotBlank
                                                                     @RequestParam(name = "caretakerId") String userId, @RequestParam(value = "page") int page,
                                                                     @RequestParam(value = "limit") int limit)
            throws UserCaretakerException {

        logger.info("Fetching patient requests  : {}",userId);

        CaretakerResponsePage userCaretakerList = careTakerService.getPatientRequests(userId, page, limit);

        return new ResponseEntity<>(userCaretakerList, HttpStatus.OK);
    }

    /**
     * Fetch all the patients of a particular caretaker
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "Fetch all the patients of a particular caretaker")
    @GetMapping(
            value = "/patients",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CaretakerResponsePage> getPatientsUnderMe(@NotBlank
                                                                    @NotNull
                                                                    @RequestParam(name = "caretakerId") String userId, @RequestParam(value = "page") int page,
                                                                    @RequestParam(value = "limit") int limit)
                                                                    throws UserCaretakerException {
        logger.info("Fetching patients  : {}",userId);

        CaretakerResponsePage userCaretakerList = careTakerService.getPatientsUnderMe(userId, page, limit);

        return new ResponseEntity<>(userCaretakerList, HttpStatus.OK);
    }

    /**
     * Deletes Patients Request for a caretaker
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "Deletes Patients Request for a caretaker")
    @GetMapping(
            value = "/delete",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CaretakerDelete> deletePatientRequest(@NotBlank
                                                         @NotNull
                                                         @RequestParam(name = "caretakerId") String caretakerId)
                                                         throws UserExceptionMessage, UserCaretakerException {
        logger.info("Deleting Request(s) : {}",caretakerId);

        careTakerService.deletePatientRequest(caretakerId);

        return new ResponseEntity<>( new CaretakerDelete(SUCCESS, DELETED_SUCCESS), HttpStatus.NO_CONTENT);
    }

    /**
     * Sends image to caretaker for strict adherence
     *
     * @param bindingResult Spring's object that holds the result of the validation and binding and contains errors that may have occurred
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "Sends image to caretaker for strict adherence")
    @PostMapping(
            value = "/image",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.ALL_VALUE
    )
    @Transactional(timeout = 10)
    public ResponseEntity<SendImageResponse> sendImageToCaretaker(@Valid
                                                                  @ModelAttribute SendImageDto sendImageDto, BindingResult bindingResult)
            throws  UserCaretakerException {

        logger.info("Sending image to caretaker : {}",sendImageDto);

        SendImageResponse sendImageResponse = careTakerService.sendImageToCaretaker(sendImageDto.getImage(),
                sendImageDto.getName(),
                sendImageDto.getMedicineName(),
                sendImageDto.getId(),
                sendImageDto.getMedicineId());

        return new ResponseEntity<>(sendImageResponse, HttpStatus.CREATED);
    }

    /**
     * Caretaker notifies user to take medicines
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "Caretaker notifies user to take medicines")
    @GetMapping(
            value = "/notify-user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Transactional(timeout = 4)
    public ResponseEntity<String> notifyUserForMedicine(@NotNull
                                                   @NotBlank
                                                   @RequestParam(name = "fcmToken") String fcmToken,
                                                   @NotNull
                                                   @NotBlank
                                                   @RequestParam("medName") String body) {

        logger.info("Notifying user(s) : {}",body);

        rabbitTemplate.convertAndSend(topicExchange, routingKeyNotification,
                new NotificationMessage(fcmToken, "Take medicine", "patient", body, ""));

        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }

}

