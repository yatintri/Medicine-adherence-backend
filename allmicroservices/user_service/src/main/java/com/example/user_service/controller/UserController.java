package com.example.user_service.controller;

import java.io.IOException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.MailInfo;
import com.example.user_service.pojos.dto.LoginDTO;
import com.example.user_service.pojos.dto.UserEntityDTO;
import com.example.user_service.pojos.response.user.UserProfileResponse;
import com.example.user_service.pojos.response.user.UserResponse;
import com.example.user_service.pojos.response.user.UserResponsePage;
import com.example.user_service.service.user.UserService;
import com.example.user_service.service.usermedicine.UserMedicineService;
import com.example.user_service.util.JwtUtil;
import com.example.user_service.util.Messages;

/**
 * This controller is used to create restful web services for user
 */
@RestController
@Validated
@RequestMapping(path = "/api/v1")
public class UserController {
    private final UserService userService;
    UserMedicineService userMedicineService;
    private final RabbitTemplate rabbitTemplate;

    private final JwtUtil jwtUtil;

    @Value("${project.rabbitmq.routingkey}")
    private String routingKey;
    @Value("${project.rabbitmq.exchange}")
    private String topicExchange;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, UserMedicineService userMedicineService,
                          RabbitTemplate rabbitTemplate,JwtUtil jwtUtil) {
        this.userMedicineService = userMedicineService;
        this.userService = userService;
        this.rabbitTemplate = rabbitTemplate;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Saves the user when they sign up
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @PostMapping(
            value = "/user",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = "application/json"
    )
    public ResponseEntity<UserResponse> saveUser(@NotNull
                                                 @NotBlank
                                                 @RequestParam(name = "fcmToken") String fcmToken, @NotNull
                                                 @NotBlank
                                                 @RequestParam(name = "picPath") String picPath, @Valid
                                                 @RequestBody UserEntityDTO userEntityDTO) throws UserExceptionMessage, UserExceptions {

        logger.info("Saving user : {}",userEntityDTO);

        return new ResponseEntity<>(userService.saveUser(userEntityDTO, fcmToken, picPath), HttpStatus.CREATED);
    }

    /**
     * Logins the user when they want to Add a caretaker
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @PostMapping(
            value = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = "application/json"
    )
    public ResponseEntity<UserResponse> login(@Valid
                                              @RequestBody LoginDTO loginDTO, BindingResult bindingResult)
                                              throws UserExceptionMessage, UserExceptions {
        logger.info("Login in the application : {}",loginDTO);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new UserResponse(Messages.VALIDATION,
                    Objects.requireNonNull(
                            bindingResult.getFieldError()).getDefaultMessage(),
                    null,
                    "",
                    ""),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(userService.login(loginDTO.getEmail(), loginDTO.getFcmToken()), HttpStatus.OK);
    }

    /**
     * Fetching the user with email if not present then sending invitation to that email address
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @GetMapping(
            value = "/email",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getUserByEmail(@NotNull
                                                 @NotBlank
                                                 @RequestParam("email") String email, @RequestParam("sender") String sender)
            throws UserExceptionMessage, UserExceptions {

        logger.info("Sending mail : {}",email);

        UserEntity userEntity = userService.getUserByEmail(email);

        if (userEntity == null) {
            rabbitTemplate.convertAndSend(topicExchange,
                    routingKey,
                    new MailInfo(email, "Please join", "patient_request", sender));

            return new ResponseEntity<>("Invitation sent to user with given email id!", HttpStatus.OK);
        }

        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    /**
     * Fetching user by id along with list of medicines
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @GetMapping(
            value = "/user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserProfileResponse> getUserById(@NotNull
                                                           @NotBlank
                                                           @RequestParam("userId") String userId)
            throws UserExceptionMessage, UserMedicineException, ExecutionException, InterruptedException,
            UserExceptions {

        logger.info("Fetching user by id : {}",userId);

        List<UserEntity> user = Arrays.asList(userService.getUserById(userId));
        List<UserMedicines> list = user.get(0).getUserMedicines();
        UserProfileResponse userProfileResponse = new UserProfileResponse("OK", user, list);

        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }

    /**
     * Fetching all the users along with details
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @GetMapping(
            value = "/users",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserResponsePage> getUsers(@RequestParam(value = "page") int page,
                                                     @RequestParam(value = "limit") int limit)
            throws UserExceptionMessage, ExecutionException, InterruptedException, UserExceptions {

        logger.info("Fetching users ");
        return new ResponseEntity<>(userService.getUsers(page, limit).get(), HttpStatus.OK);
    }

    /**
     * This allows you to have short-lived access tokens without having to collect credentials every time one expires.
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @PostMapping(
            value = "/refreshToken",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = "application/json"
    )
    public ResponseEntity<String> refreshToken(@NotNull
                                               @NotBlank
                                               @RequestParam(name = "uid") String uid, HttpServletRequest httpServletRequest, BindingResult bindingResult)
            throws UserExceptionMessage, UserExceptions, UserMedicineException, ExecutionException,
            InterruptedException {

        logger.info("Generating new token : {}",uid);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(Messages.VALIDATION, HttpStatus.BAD_REQUEST);
        }

        String jwtToken = jwtUtil.generateToken(userService.getUserById(uid).getUserName());

        return new ResponseEntity<>(jwtToken, HttpStatus.CREATED);
    }

    /**
     * Generates a pdf for the adherence maintained by a user
     */
    @GetMapping(
            value = "/pdf",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.ALL_VALUE
    )
    @Transactional(timeout = 10)
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    public ResponseEntity<UserResponse> sendPdf(@NotNull
                                                @NotBlank
                                                @RequestParam(name = "medId") Integer medId)
            throws IOException, MessagingException, UserExceptionMessage, UserExceptions {

        logger.info("Sending pdf : {}",medId);

        if (userService.sendUserMedicines(medId).equals(Messages.FAILED)) {
            throw new UserExceptionMessage(Messages.MEDICINE_NOT_FOUND);
        }

        String filePath = userService.sendUserMedicines(medId);
        UserResponse userResponse = new UserResponse(Messages.SUCCESS, filePath, null, "", "");

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }


}

