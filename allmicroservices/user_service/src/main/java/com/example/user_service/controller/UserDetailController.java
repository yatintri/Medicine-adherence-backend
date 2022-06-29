package com.example.user_service.controller;



import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.model.UserDetails;
import com.example.user_service.pojos.dto.UserDetailsDTO;
import com.example.user_service.pojos.response.UserDetailResponse;
import com.example.user_service.service.CareTakerService;
import com.example.user_service.service.UserDetailService;
import com.example.user_service.service.UserService;
import com.example.user_service.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
public class UserDetailController {



    private final UserDetailService userDetailService;

    public UserDetailController(UserDetailService userDetailService){
        this.userDetailService = userDetailService;
    }

    @PutMapping(value = "/user-details" , produces = MediaType.APPLICATION_JSON_VALUE,consumes = "application/json")
    public ResponseEntity<UserDetailResponse> updateUserDetails(@NotBlank @NotNull @RequestParam("userId") String id,
                                                                @Valid @RequestBody UserDetailsDTO userDetailsDTO, BindingResult bindingResult) throws UserExceptionMessage, UserExceptions {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new UserDetailResponse(Messages.VALIDATION, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(),null),HttpStatus.BAD_REQUEST);
        }
        UserDetails userDetails = userDetailService.saveUserDetail(id,userDetailsDTO);
        UserDetailResponse userDetailResponse= new UserDetailResponse(Messages.SUCCESS,Messages.SAVE_DETAILS,userDetails);
        return new ResponseEntity<>(userDetailResponse,HttpStatus.OK);

    }

}
