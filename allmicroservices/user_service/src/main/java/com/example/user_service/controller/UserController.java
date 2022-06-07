package com.example.user_service.controller;


import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.MailInfo;
import com.example.user_service.pojos.dto.LoginDTO;
import com.example.user_service.pojos.dto.UserEntityDTO;
import com.example.user_service.pojos.response.UserProfileResponse;
import com.example.user_service.pojos.response.UserResponse;
import com.example.user_service.service.UserMedicineService;
import com.example.user_service.service.UserService;
import com.example.user_service.util.JwtUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping(path = "/api/v1")
public class UserController {

    private static final String MSG = "Success";

    @Autowired
    private UserService userService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserMedicineService userMedicineService;

    // saving the user when they signup
    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> saveUser(@RequestParam(name = "fcmToken") String fcmToken, @RequestParam(name = "picPath") String picPath, @RequestBody UserEntityDTO userEntityDTO) throws UserExceptionMessage{

        return new ResponseEntity<>(userService.saveUser(userEntityDTO, fcmToken, picPath), HttpStatus.CREATED);


    }

    @PostMapping("/refreshToken")
    public ResponseEntity<String> refreshToken(@Valid @RequestParam(name = "uid") String uid, HttpServletRequest httpServletRequest) throws UserExceptionMessage, UserMedicineException, ExecutionException, InterruptedException {

        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String jwtToken = jwtUtil.generateToken(userService.getUserById(uid).getUserName());

        return new ResponseEntity<>(jwtToken, HttpStatus.CREATED);

    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> login(@RequestBody LoginDTO loginDTO) throws UserExceptionMessage {

        return new ResponseEntity<>(userService.login(loginDTO.getEmail(),loginDTO.getFcmToken()), HttpStatus.OK);


    }


    // fetching all the users along with details
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<List<UserEntity>> getUsers() throws UserExceptionMessage, ExecutionException, InterruptedException {

        return new ResponseEntity<>(userService.getUsers().get(), HttpStatus.OK);


    }

    // fetching user by id
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileResponse> getUserById(@RequestParam("userId") String userId) throws UserExceptionMessage, UserMedicineException, ExecutionException, InterruptedException {


        List<UserEntity> user = Arrays.asList(userService.getUserById(userId));
        List<UserMedicines> list = user.get(0).getUserMedicines();

        UserProfileResponse userProfileResponse = new UserProfileResponse("OK", user, list);
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);


    }

    // fetching the user with email if not present then sending to that email address
    @GetMapping(value = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends Object> getUserByEmail(@RequestParam("email") String email
            , @RequestParam("sender") String sender)
            throws UserExceptionMessage {

        UserEntity userEntity = userService.getUserByEmail(email);
        if (userEntity == null) {
            rabbitTemplate.convertAndSend("project_exchange",
                    "mail_key", new MailInfo(email, "Please join", "patient_request", sender));
            return new ResponseEntity<>("Invitation sent to user with given email id!", HttpStatus.OK);

        }
        return new ResponseEntity<>(userEntity, HttpStatus.OK);

    }


    @GetMapping(value = "/pdf")
    public ResponseEntity<UserResponse> sendPdf(@RequestParam(name = "medId") Integer medId) throws IOException, MessagingException, UserExceptionMessage {
       if(userService.sendUserMedicines(medId).equals("Failed")){
           throw new UserExceptionMessage("No medicine found!");
       }
        String filePath = userService.sendUserMedicines(medId);
        UserResponse userResponse = new UserResponse(MSG, filePath, null, "", "");
        return new ResponseEntity<>(userResponse, HttpStatus.OK);

    }


}
