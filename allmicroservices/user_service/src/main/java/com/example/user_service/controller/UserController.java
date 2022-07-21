package com.example.user_service.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.user_service.model.User;
import com.example.user_service.pojos.dto.response.RefreshTokenResponse;
import com.example.user_service.util.Constants;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.MailInfo;
import com.example.user_service.pojos.dto.request.LoginDTO;
import com.example.user_service.pojos.dto.request.UserEntityDTO;
import com.example.user_service.pojos.dto.response.user.UserProfileResponse;
import com.example.user_service.pojos.dto.response.user.UserResponse;
import com.example.user_service.pojos.dto.response.user.UserResponsePage;
import com.example.user_service.service.UserService;
import com.example.user_service.service.UserMedicineService;
import com.example.user_service.util.JwtUtil;

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
                          RabbitTemplate rabbitTemplate, JwtUtil jwtUtil) {
        this.userMedicineService = userMedicineService;
        this.userService = userService;
        this.rabbitTemplate = rabbitTemplate;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Saves the user when they sign up
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "Saves the user when they sign up")
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
                                                 @RequestBody UserEntityDTO userEntityDTO) {

        logger.info("Saving user : {}", userEntityDTO);

        return new ResponseEntity<>(userService.saveUser(userEntityDTO, fcmToken, picPath), HttpStatus.CREATED);
    }

    /**
     * This allows you to have short-lived access tokens without having to collect credentials every time one expires.
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "This allows you to have short-lived access tokens without having to collect credentials every time one expires.")
    @PostMapping(value = "/refreshToken",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RefreshTokenResponse> getRefreshToken(HttpServletRequest httpServletRequest, @RequestParam(name = "userId") String userId) throws UserExceptionMessage{
        return new ResponseEntity<>(userService.getRefreshToken(httpServletRequest,userId),HttpStatus.OK);
    }

    /**
     * Logins the user when they want to Add a caretaker
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "Logins the user when they want to Add a caretaker")
    @PostMapping(
            value = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = "application/json"
    )
    public ResponseEntity<UserResponse> login(@Valid
                                              @RequestBody LoginDTO loginDTO)
            {

        logger.info("Login in the application : {}", loginDTO);

        return new ResponseEntity<>(userService.login(loginDTO.getEmail(), loginDTO.getFcmToken()), HttpStatus.CREATED);
    }

    /**
     * Fetching the user with email if not present then sending invitation to that email address
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "Fetching the user with email if not present then sending invitation to that email address")
    @GetMapping(
            value = "/email",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getUserByEmail(@NotNull
                                                 @NotBlank
                                                 @RequestParam("email") String email, @RequestParam("sender") String sender)
            {

        logger.info("Sending mail : {}", email);

        User userEntity = userService.getUserByEmail(email);

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
    @ApiOperation(value = "Fetching user by id along with list of medicines")
    @GetMapping(
            value = "/user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserProfileResponse> getUserById(@NotNull
                                                           @NotBlank
                                                           @RequestParam("userId") String userId)
            {

        logger.info("Fetching user by id : {}", userId);

        List<User> user = Arrays.asList(userService.getUserById(userId));
        List<UserMedicines> list = user.get(0).getUserMedicines();
        UserProfileResponse userProfileResponse = new UserProfileResponse("OK", user, list);

        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }

    /**
     * Fetching all the users along with details
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "Fetching all the users along with details")
    @GetMapping(
            value = "/users",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserResponsePage> getUsers(@RequestParam(value = "page") int page,
                                                     @RequestParam(value = "limit") int limit){

        logger.info("Fetching users ");
        return new ResponseEntity<>(userService.getUsers(page, limit), HttpStatus.OK);
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
    @ApiOperation(value = "Generates a pdf for the adherence maintained by a user")
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    public ResponseEntity<UserResponse> sendPdf(@NotNull
                                                @NotBlank
                                                @RequestParam(name = "medId") Integer medId)
            {

        logger.info("Sending pdf : {}", medId);

        if (userService.sendUserMedicines(medId).equals(Constants.FAILED)) {
            throw new UserExceptionMessage(Constants.MEDICINE_NOT_FOUND);
        }

        String filePath = userService.sendUserMedicines(medId);
        UserResponse userResponse = new UserResponse(Constants.SUCCESS, filePath, null, "", "");

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }


}

