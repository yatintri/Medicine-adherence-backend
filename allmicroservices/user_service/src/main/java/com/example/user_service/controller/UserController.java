package com.example.user_service.controller;


import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.MailInfo;
import com.example.user_service.pojos.response.UserProfileResponse;
import com.example.user_service.pojos.response.UserResponse;
import com.example.user_service.pojos.dto.UserEntityDTO;
import com.example.user_service.repository.Medrepo;
import com.example.user_service.service.UserMedicineService;
import com.example.user_service.service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping(path = "/api/v1")
public class UserController {


    private String msg="Success";
    @Autowired
    private UserService userService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    Medrepo medrepo;

    @Autowired
    UserMedicineService userMedicineService;
    // saving the user when they signup
    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> saveUser(@RequestParam (name = "fcmToken")String fcmToken , @RequestParam (name = "picPath")String picPath , @RequestBody UserEntityDTO userEntityDTO) throws UserexceptionMessage, ExecutionException, InterruptedException {
        UserEntity user = userService.getUserByEmail(userEntityDTO.getEmail());
        if(user != null){
            UserResponse userresponse = new UserResponse(msg,"User is already present",new ArrayList<>(Arrays.asList(user)));
            return new ResponseEntity<>(userresponse, HttpStatus.FOUND);
        }
        user = userService.saveUser(userEntityDTO,fcmToken,picPath).get();
        UserResponse userresponse = new UserResponse(msg,"Saved user successfully",new ArrayList<>(Arrays.asList(user)));

        return new ResponseEntity<>(userresponse, HttpStatus.CREATED);


    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> login(@RequestParam String email) throws UserexceptionMessage {
        UserEntity user = userService.getUserByEmail(email);
        if(user != null){
            UserResponse userresponse = new UserResponse(msg,"Logged in successfully",new ArrayList<>(Arrays.asList(user)));
            return new ResponseEntity<>(userresponse, HttpStatus.FOUND);
        }
        UserResponse userresponse = new UserResponse("Failed","User not found by this email",null);

        return new ResponseEntity<>(userresponse, HttpStatus.NOT_FOUND);

    }


    // fetching all the users along with details
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserEntity>> getUsers() throws UserexceptionMessage, ExecutionException, InterruptedException {

        return new ResponseEntity<>(userService.getUsers().get(), HttpStatus.OK);


    }

    // fetching user by id
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileResponse> getUserById(@RequestParam("id") String userId) throws UserexceptionMessage, UserMedicineException, ExecutionException, InterruptedException {


        List<UserEntity> user = Arrays.asList(userService.getUserById(userId));
        List<UserMedicines> list = userMedicineService.getallUserMedicines(userId).get();

        UserProfileResponse userProfileResponse = new UserProfileResponse(msg,user,list);
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);



    }

    // updating the user
    @PutMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> updateUser(@RequestParam("id") String userId
            , @RequestBody UserEntityDTO userEntityDTO) throws UserexceptionMessage {
        UserEntity user= userService.updateUser(userId, userEntityDTO);
        UserResponse userResponse= new UserResponse(msg,"Updated successfully",new ArrayList<>(Arrays.asList(user)));
        return new ResponseEntity<>(userResponse, HttpStatus.OK);


    }


    // fetching the user with email if not present then sending to that email address
    @GetMapping(value = "/user/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam("email") String email
            , @RequestParam("sender") String sender)
            throws UserexceptionMessage {

        UserEntity userEntity = userService.getUserByEmail(email);
        UserResponse userResponse = new UserResponse(msg,"Data found",new ArrayList<>(Arrays.asList(userEntity)));
        if(userEntity == null){
            rabbitTemplate.convertAndSend("project_exchange",
                    "mail_key",new MailInfo(email,"Please join","patient_request",sender));
            UserResponse userResponse1 = new UserResponse("Failed","Email not found", null);
            return new ResponseEntity<>(userResponse1, HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(userResponse,HttpStatus.OK);

    }





}