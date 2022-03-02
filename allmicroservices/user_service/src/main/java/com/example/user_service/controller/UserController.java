package com.example.user_service.controller;


import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.MedicineEntity;
import com.example.user_service.model.UserEntity;
import com.example.user_service.pojos.MailInfo;
import com.example.user_service.repository.Medrepo;
import com.example.user_service.service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.SendFailedException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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

    // saving the user when they signup
    @PostMapping(value = "/saveuser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@RequestBody UserEntity userEntity) throws UserexceptionMessage {

        return new ResponseEntity<>(userService.saveUser(userEntity), HttpStatus.CREATED);


    }

    // fetching all the users along with details
    @GetMapping(value = "/getusers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserEntity>> getUsers() throws UserexceptionMessage, ExecutionException, InterruptedException {

        return new ResponseEntity(userService.getUsers().get(), HttpStatus.OK);


    }

    // fetching user by id
    @GetMapping(value = "/getuser/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable("id") String user_id) throws UserexceptionMessage {


        return new ResponseEntity<>(userService.getUserById(user_id), HttpStatus.OK);

    }

    // updating the user
    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@PathVariable("id") String user_id
            , @RequestBody UserEntity userEntity) throws UserexceptionMessage {

        return new ResponseEntity<>(userService.updateUser(user_id, userEntity), HttpStatus.OK);


    }

    // fetching user by name
    @GetMapping(value = "/getuser/byname", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserByName(@RequestParam("name") String user_name) throws UserexceptionMessage {

        return new ResponseEntity<>(userService.getUserByName(user_name), HttpStatus.OK);

    }

    // fetching the user with email if not present then sending to that email address
    @GetMapping(value = "/getbyemail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserByEmail(@RequestParam("email") String email
                                        ,@RequestParam("sender") String sender)
                                        throws UserexceptionMessage , SendFailedException {

       UserEntity userEntity = userService.getUserByEmail(email);
       if(userEntity == null){

         rabbitTemplate.convertAndSend("project_exchange",
                 "mail_key",new MailInfo(email,"Please join","patient_request",sender));
          return new ResponseEntity<>("Invitation sent to user with given email id!" , HttpStatus.OK);

       }
        return new ResponseEntity<>(userEntity, HttpStatus.OK);

    }


    @GetMapping(value = "/getmeds")
    public String savemed() throws IOException {

        BufferedReader bufferedReader = new BufferedReader
                (new FileReader("/home/nineleaps/Medicine-adherence-backend/allmicroservices/user_service/src/main/resources/drugs.tsv"));
        HashSet<String> set = new HashSet<>();
        String d = "";
        while((d = bufferedReader.readLine()) != null){
            set.add(d.split(" ")[0]);
        }
        System.out.println(set);
        List<MedicineEntity> list = new ArrayList<>();
        for(String m : set){
            MedicineEntity medicineEntity = new MedicineEntity();
            medicineEntity.setMed_name(m);

            list.add(medicineEntity);
        }

        medrepo.saveAll(list);

        return "Ho gaya";
//
    }

    @GetMapping(value = "/searchmed")
    public ResponseEntity<?> getallmeds(@RequestParam(name = "search_med") String search_med){

        return new ResponseEntity<>(medrepo.getmedicines(search_med),HttpStatus.OK);

    }


}
///