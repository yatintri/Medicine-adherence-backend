package com.example.user_service.controller;


import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.MailInfo;
import com.example.user_service.pojos.Userresponse;
import com.example.user_service.pojos.dto.UserEntityDTO;
import com.example.user_service.pojos.response.UserProfileResponse;
import com.example.user_service.repository.Medrepo;
import com.example.user_service.service.UserMedicineService;
import com.example.user_service.service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.SendFailedException;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    Medrepo medrepo;

    @Autowired
    UserMedicineService userMedicineService;
    // saving the user when they signup
    @PostMapping(value = "/saveuser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Userresponse> saveUser(@RequestParam (name = "fcmToken")String fcmToken ,@RequestParam (name = "picPath")String picPath , @RequestBody UserEntityDTO userEntityDTO) throws UserexceptionMessage, ExecutionException, InterruptedException {
        UserEntity user = userService.getUserByEmail(userEntityDTO.getEmail());
        if(user != null){
            Userresponse userresponse = new Userresponse("Already present",user);
            return new ResponseEntity<>(userresponse, HttpStatus.CREATED);
        }
        user = userService.saveUser(userEntityDTO,fcmToken,picPath).get();
        Userresponse userresponse = new Userresponse("success",user);

        return new ResponseEntity<>(userresponse, HttpStatus.CREATED);


    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Userresponse> login(@RequestParam String email) throws UserexceptionMessage {
        UserEntity user = userService.getUserByEmail(email);
        if(user != null){
            Userresponse userresponse = new Userresponse("success",user);
            return new ResponseEntity<>(userresponse, HttpStatus.CREATED);
        }
        Userresponse userresponse = new Userresponse("Not found",null);

        return new ResponseEntity<>(userresponse, HttpStatus.CREATED);


    }


    // fetching all the users along with details
    @GetMapping(value = "/getusers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserEntity>> getUsers() throws UserexceptionMessage, ExecutionException, InterruptedException {

        return new ResponseEntity<>(userService.getUsers().get(), HttpStatus.OK);


    }

    // fetching user by id
    @GetMapping(value = "/getuser/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable("id") String userId) throws UserexceptionMessage, UserMedicineException, ExecutionException, InterruptedException {


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
    @GetMapping(value = "/getbyemail", produces = MediaType.APPLICATION_JSON_VALUE)
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





}
//////