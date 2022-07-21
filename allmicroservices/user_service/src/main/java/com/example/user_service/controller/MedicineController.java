package com.example.user_service.controller;

import java.util.List;


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

import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.pojos.dto.request.MedicineHistoryDTO;
import com.example.user_service.pojos.dto.request.MedicinePojo;
import com.example.user_service.pojos.dto.response.image.ImageListResponse;
import com.example.user_service.pojos.dto.response.medicine.MedicineResponse;
import com.example.user_service.pojos.dto.response.medicine.SyncResponse;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserMedicineService;

import static com.example.user_service.util.Constants.UNABLE_TO_SYNC;

/**
 * This controller is used to create restful web services for user medicines
 */
@RestController
@Validated
@RequestMapping(path = "/api/v1/")
public class MedicineController {
    private final UserMedicineService userMedicineService;
    UserRepository userRepository;
    UserMedicineRepository userMedicineRepository;

    private final Logger logger = LoggerFactory.getLogger(MedicineController.class);


    public MedicineController(UserMedicineService userMedicineService, UserRepository userRepository,
                              UserMedicineRepository userMedicineRepository) {
        this.userMedicineService = userMedicineService;
        this.userRepository = userRepository;
        this.userMedicineRepository = userMedicineRepository;
    }

    /**
     * Syncs local storage data of the application with the server
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "Syncs local storage data of the application with the server")
    @PostMapping(
            value = "/medicines/sync",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = "application/json"
    )
    public ResponseEntity<SyncResponse> syncData(@NotBlank
                                                 @NotNull
                                                 @RequestParam("userId") String userId, @Valid
                                                 @RequestBody List<MedicinePojo> medicinePojo)
             {

        logger.info("Syncing Data : {} {}",userId,medicinePojo);

        return new ResponseEntity<>(userMedicineService.syncData(userId, medicinePojo), HttpStatus.CREATED);
    }

    /**
     * Syncs medicine history of all the medicines from local storage to backend
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "Syncs medicine history of all the medicines from local storage to backend")
    @PostMapping(
            value = "/medicine-history/sync",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = "application/json"
    )
    public ResponseEntity<SyncResponse> syncMedicineHistory(@NotNull
                                                            @NotBlank
                                                            @RequestParam(name = "medId") Integer medId, @Valid
                                                            @RequestBody List<MedicineHistoryDTO> medicineHistory)
             {

        logger.info("Syncing medicine history : {} {}",medId,medicineHistory);

        try {
            userMedicineService.syncMedicineHistory(medId, medicineHistory);

            return new ResponseEntity<>(new SyncResponse(Constants.SUCCESS, Constants.SYNC), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new UserMedicineException(UNABLE_TO_SYNC);
        }
    }

    /**
     * Fetch all medicines for a user by id
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "Fetch all medicines for a user by id")
    @GetMapping(
            value = "/medicine-histories",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MedicineResponse> getMedicineHistories(@NotBlank
                                                                 @NotNull
                                                                 @RequestParam(name = "medId") Integer medId, @RequestParam(value = "page") int page,
                                                                 @RequestParam(value = "limit") int limit)
                                                                  {
        logger.info("Fetching medicine history : {}",medId);

        return new ResponseEntity<>(userMedicineService.getMedicineHistory(medId, page, limit), HttpStatus.OK);
    }

    /**
     * Fetches all the images stored for that medicine
     */
    @Retryable(maxAttempts = 3)    // retrying up to 3 times
    @ApiOperation(value = "Fetches all the images stored for that medicine")
    @GetMapping(
            value = "/medicine-images",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ImageListResponse> getMedicineImages(@NotBlank
                                                               @NotNull
                                                               @RequestParam(name = "medId") Integer medId
                                                              , @RequestParam(value = "page") int page,
                                                               @RequestParam(value = "limit") int limit)
                                                               {
        logger.info("Fetching medicine images : {}",medId);

        return new ResponseEntity<>(userMedicineService.getUserMedicineImages(medId, page, limit), HttpStatus.OK);
    }
}

