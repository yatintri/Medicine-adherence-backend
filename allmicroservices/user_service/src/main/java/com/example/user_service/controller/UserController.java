package com.example.user_service.controller;

import javax.servlet.http.HttpServletRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.user_service.pojos.response.user.MailResponse;
import com.example.user_service.pojos.response.user.RefreshTokenResponse;
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
import com.example.user_service.pojos.request.LoginDTO;
import com.example.user_service.pojos.request.UserEntityDTO;
import com.example.user_service.pojos.response.user.UserProfileResponse;
import com.example.user_service.pojos.response.user.UserResponse;
import com.example.user_service.pojos.response.user.UserResponsePage;
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

    @Value("${project.rabbitmq.routingKey}")
    private String routingKey;
    @Value("${project.rabbitmq.exchange}")
    private String topicExchange;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);


    public UserController(UserService userService, UserMedicineService userMedicineService
                          ) {
        this.userMedicineService = userMedicineService;
        this.userService = userService;

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
    public ResponseEntity<RefreshTokenResponse> getRefreshToken(HttpServletRequest httpServletRequest, @RequestParam(name = "userId") String userId) {
        return new ResponseEntity<>(userService.getRefreshToken(httpServletRequest,userId),HttpStatus.CREATED);
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
    public ResponseEntity<MailResponse> getUserByEmail(@NotNull
                                                 @NotBlank
                                                 @RequestParam("email") String email, @RequestParam("sender") String sender)
            {

        logger.info("Sending mail : {}", email);

        return new ResponseEntity<>(userService.getUserByEmail(email,sender),HttpStatus.OK);
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

        return new ResponseEntity<>(userService.getUserMedicine(userId), HttpStatus.OK);
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
                                                @RequestParam(name = "medicineId") Integer medicineId)
            {

        logger.info("Sending pdf : {}", medicineId);

        if (userService.sendUserMedicines(medicineId).equals(Constants.FAILED)) {
            throw new UserExceptionMessage(Constants.MEDICINE_NOT_FOUND);
        }

        String filePath = userService.sendUserMedicines(medicineId);
        UserResponse userResponse = new UserResponse(Constants.SUCCESS, filePath, null, "", "");

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }


}

