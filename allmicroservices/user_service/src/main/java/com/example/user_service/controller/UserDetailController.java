package com.example.user_service.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.user_service.util.Constants;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.user_service.model.UserDetails;
import com.example.user_service.pojos.dto.request.UserDetailsDTO;
import com.example.user_service.pojos.dto.response.user.UserDetailResponse;
import com.example.user_service.service.UserDetailService;

/**
 * The controller class is responsible for processing incoming REST API requests
 */
@RestController
@Validated
@RequestMapping("/api/v1")
public class UserDetailController {
    private final UserDetailService userDetailService;

    private final Logger logger = LoggerFactory.getLogger(UserDetailController.class);
    public UserDetailController(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    /**
     * Updates user details with respect to its id
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "Updates user details with respect to its id")
    @PutMapping(
            value = "/user-details",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = "application/json"
    )
    public ResponseEntity<UserDetailResponse> updateUserDetails(@NotBlank
                                                                @NotNull
                                                                @RequestParam("userId") String id, @Valid
                                                                @RequestBody UserDetailsDTO userDetailsDTO)
             {

        logger.info("Updating user details : {} {}",id,userDetailsDTO);
        UserDetails userDetails = userDetailService.saveUserDetail(id, userDetailsDTO);
        UserDetailResponse userDetailResponse = new UserDetailResponse(Constants.SUCCESS,
                Constants.SAVE_DETAILS,
                userDetails);

        return new ResponseEntity<>(userDetailResponse, HttpStatus.OK);
    }
}

