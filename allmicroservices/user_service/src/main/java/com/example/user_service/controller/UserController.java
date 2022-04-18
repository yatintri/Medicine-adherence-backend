package com.example.user_service.controller;


import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.MailInfo;
import com.example.user_service.pojos.Userresponse;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping(path = "/api/v1")
public class UserController {

    private static final String MSG="Success";

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
    public ResponseEntity<UserResponse> saveUser(@RequestParam (name = "fcmToken")String fcmToken ,@RequestParam (name = "picPath")String picPath , @RequestBody UserEntityDTO userEntityDTO) throws UserexceptionMessage, ExecutionException, InterruptedException {
        UserEntity user = userService.getUserByEmail(userEntityDTO.getEmail());
        if(user != null){
            UserResponse userresponse = new UserResponse(MSG,"User is already present",new ArrayList<>(Arrays.asList(user)),"","");
            return new ResponseEntity<>(userresponse, HttpStatus.CREATED);
        }
        user = userService.saveUser(userEntityDTO,fcmToken,picPath).get();
        String jwtToken = jwtUtil.generateToken(user.getUserName());
        String refreshToken = passwordEncoder.encode(user.getUserId());

        UserResponse userresponse = new UserResponse(MSG,"Saved user successfully",new ArrayList<>(Arrays.asList(user)),jwtToken,refreshToken);

        return new ResponseEntity<>(userresponse, HttpStatus.CREATED);


    }
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestParam (name= "uid")String uid, HttpServletRequest httpServletRequest) throws UserexceptionMessage, UserMedicineException, ExecutionException, InterruptedException {
        //String requestRefreshToken = request.getRefreshToken();
        String token =  httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        System.out.println(passwordEncoder.matches(uid, token));
        String jwtToken = jwtUtil.generateToken(userService.getUserById(uid).getUserName());

        return new ResponseEntity<>(jwtToken, HttpStatus.CREATED);

    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Userresponse> login(@RequestParam String email) throws UserexceptionMessage {
        UserEntity user = userService.getUserByEmail(email);
        if(user != null){
            Userresponse userresponse = new Userresponse(MSG,user);
            return new ResponseEntity<>(userresponse, HttpStatus.CREATED);
        }
        Userresponse userresponse = new Userresponse("Not found",null);

        return new ResponseEntity<>(userresponse, HttpStatus.CREATED);


    }


    // fetching all the users along with details
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserEntity>> getUsers() throws UserexceptionMessage, ExecutionException, InterruptedException {

        return new ResponseEntity<>(userService.getUsers().get(), HttpStatus.OK);


    }

    // fetching user by id
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileResponse> getUserById(@RequestParam("userId") String userId) throws UserexceptionMessage, UserMedicineException, ExecutionException, InterruptedException {


        List<UserEntity> user = Arrays.asList(userService.getUserById(userId));
        List<UserMedicines> list = userMedicineService.getallUserMedicines(userId).get();

        UserProfileResponse userProfileResponse = new UserProfileResponse("OK",user,list);
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);



    }

    // updating the user
    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserEntity> updateUser(@PathVariable("id") String userId
            , @RequestBody UserEntityDTO userEntity) throws UserexceptionMessage {

        return new ResponseEntity<>(userService.updateUser(userId, userEntity), HttpStatus.OK);


    }


    // fetching the user with email if not present then sending to that email address
    @GetMapping(value = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends Object> getUserByEmail(@RequestParam("email") String email
            ,@RequestParam("sender") String sender)
            throws UserexceptionMessage {

        UserEntity userEntity = userService.getUserByEmail(email);
        if(userEntity == null){
            rabbitTemplate.convertAndSend("project_exchange",
                    "mail_key",new MailInfo(email,"Please join","patient_request",sender));
            return new ResponseEntity<>("Invitation sent to user with given email id!" , HttpStatus.OK);

        }
        return new ResponseEntity<>(userEntity, HttpStatus.OK);

    }


    @GetMapping(value = "/sendpdf")
    public ResponseEntity<UserResponse> sendpdf(@RequestParam(name = "userId") String userId) throws IOException, MessagingException {
        String filePath= userService.sendUserMedicines(userId);
        UserResponse userResponse= new UserResponse("Success",filePath,null,"","");
        return new ResponseEntity<>(userResponse, HttpStatus.OK);

    }




}
//////