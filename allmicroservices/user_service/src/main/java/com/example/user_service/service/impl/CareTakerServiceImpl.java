package com.example.user_service.service.impl;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import com.example.user_service.pojos.NotificationMessage;
import com.example.user_service.pojos.response.caretaker.CaretakerDelete;
import com.example.user_service.service.CareTakerService;
import com.example.user_service.util.Constants;
import com.example.user_service.util.DateHelper;
import org.hibernate.exception.JDBCConnectionException;

import org.modelmapper.ModelMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.user_service.exception.UserCaretakerException;
import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.model.Image;
import com.example.user_service.model.UserCaretaker;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.request.UserCaretakerDTO;
import com.example.user_service.pojos.response.caretaker.CaretakerResponse;
import com.example.user_service.pojos.response.caretaker.CaretakerResponse1;
import com.example.user_service.pojos.response.caretaker.CaretakerResponsePage;
import com.example.user_service.pojos.response.image.SendImageResponse;
import com.example.user_service.repository.ImageRepository;
import com.example.user_service.repository.UserCaretakerRepository;
import com.example.user_service.repository.UserMedicineRepository;

import static com.example.user_service.util.Constants.*;

/**
 * This class contains all the business logic for the caretaker controller
 */
@Service
public class CareTakerServiceImpl implements CareTakerService {
    Logger logger = LoggerFactory.getLogger(CareTakerServiceImpl.class);

    private final UserCaretakerRepository userCaretakerRepository;

    private final ModelMapper mapper;

    private final ImageRepository imageRepository;

    private final UserMedicineRepository userMedicineRepository;

    RabbitTemplate rabbitTemplate;
    @Value("${project.rabbitmq.routingKeyNotification}")
    private String routingKeyNotification;
    @Value("${project.rabbitmq.exchange}")
    private String topicExchange;

    public CareTakerServiceImpl(ImageRepository imageRepository, UserMedicineRepository userMedicineRepository,
                                RabbitTemplate rabbitTemplate, UserCaretakerRepository userCaretakerRepository,
                                ModelMapper mapper) {
        this.imageRepository = imageRepository;
        this.userMedicineRepository = userMedicineRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.userCaretakerRepository = userCaretakerRepository;
        this.mapper = mapper;
    }

    /**
     * This method deletes patient request sent to a caretaker
     */
    @Override
    @CacheEvict(value = "caretakerCache",key = "#cId")
    public CaretakerDelete deletePatientRequest(String id){
        logger.info(Constants.STARTING_METHOD_EXECUTION);

        try {
            Optional<UserCaretaker> userCaretaker = userCaretakerRepository.findById(id);

            if (userCaretaker.isPresent()) {
                userCaretaker.get().setDelete(true);
                userCaretakerRepository.save(userCaretaker.get());

                return new CaretakerDelete(Constants.SUCCESS,Constants.DELETED_SUCCESS);
            }

            logger.info(EXITING_METHOD_EXECUTION);
            logger.debug("Deleted {} request",id);

            throw new UserCaretakerException(MSG);
        } catch (Exception e) {
            logger.error("Delete patient request " + MSG);
            throw new UserCaretakerException(MSG);
        }
    }

    /**
     * This method contains the logic to save a caretaker
     */
    @Override
    public CaretakerResponse saveCareTaker(@Valid UserCaretakerDTO userCaretakerDTO)
             {
        logger.info(Constants.STARTING_METHOD_EXECUTION);

        try {
            UserCaretaker userCaretaker = mapToEntity(userCaretakerDTO);

            userCaretaker.setCreatedAt(DateHelper.getCurrentDatetime());
            userCaretaker.setUpdatedAt(DateHelper.getCurrentDatetime());

            if (userCaretakerRepository.check(userCaretaker.getPatientId(), userCaretaker.getCaretakerId()) != null) {

                logger.error("Save Caretaker: Caretaker already present");
                throw new UserCaretakerException(ALREADY_PRESENT);

            } else {
                userCaretakerRepository.save(userCaretaker);
                logger.info(EXITING_METHOD_EXECUTION);
                logger.debug("Saving {} caretaker",userCaretakerDTO);
                return new CaretakerResponse(SUCCESS, REQ_SENT_SUCCESS, userCaretaker);
            }
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {

            logger.error("Caretaker :" + SQL_ERROR_MSG);
            throw new UserExceptionMessage(SQL_ERROR_MSG);
        }
    }

    /**
     * This method uses fcm to send image to caretaker as a notification message
     */
    @Override
    public SendImageResponse sendImageToCaretaker(MultipartFile multipartFile, String filename, String medicineName,
                                                  String caretakerId, Integer medicineId)
             {
        logger.info(Constants.STARTING_METHOD_EXECUTION);

        try {
            Path path = Paths.get(System.getProperty("user.dir") + "/src/main/upload/static/images",
                    filename.concat(".").concat("jpg"));

            Files.write(path, multipartFile.getBytes());

            UserMedicines userMedicines = userMedicineRepository.getMedicineById(medicineId);
            if(userMedicines == null){
                throw new UserCaretakerException(Constants.NO_MEDICINE_FOUND);

            }
            String userName = userMedicines.getUserEntity().getUserName();
            Image image = new Image();
            image.setCreatedAt(LocalDateTime.now());
            image.setImageUrl(path.getFileName().toString());
            image.setTime(Calendar.getInstance().getTime().toString());
            image.setDate(Calendar.getInstance().getTime());
            image.setUserMedicines(userMedicines);
            image.setCaretakerName(userName);
            imageRepository.save(image);

            String fcmToken =
                    "epkw4MI-RxyMzZjvD6fUl6:APA91bEUyAJpJ5RmDyI1KLcMLJbBPiYSX64oIW4WkNq62zeUlMPUPknGkBHTB_drOBX6CUkiI0Pyfc4Myvt87v6BU69kz0LPq4YM9iWnG9RrNbxIpC4LrtE-zWfNdbB3dbjR2bmogops";

            rabbitTemplate.convertAndSend(topicExchange,
                    routingKeyNotification,
                    new NotificationMessage(fcmToken,
                            "Take medicine",
                            "caretaker",
                            medicineName,
                            filename + ".jpg"));
        } catch (Exception e) {
            logger.info(e.getMessage());

            return new SendImageResponse(Constants.FAILED, Constants.UNABLE_TO_SEND);
        }
        logger.info(EXITING_METHOD_EXECUTION);
        logger.debug("Sending image {} to {} caretaker for {} medicine",filename,caretakerId,medicineId);
        return new SendImageResponse(Constants.SUCCESS, Constants.SENT_SUCCESSFULLY);
    }

    /**
     * This method contains logic which updates caretaker request status
     */
    @Override
    @CachePut(value = "caretakerCache",key = "#cId")
    public CaretakerResponse updateCaretakerStatus(String id)  {
        logger.info(Constants.STARTING_METHOD_EXECUTION);

        try {
            Optional<UserCaretaker> uc = userCaretakerRepository.findById(id);

            if (uc.isEmpty() || Objects.isNull(uc.get().getId())) {
                logger.error("Update Caretaker Status: Data not found");

                throw new UserCaretakerException(MSG);
            }
            uc.get().setUpdatedAt(DateHelper.getCurrentDatetime());
            uc.get().setRequestStatus(true);
            userCaretakerRepository.save(uc.get());
            logger.info(EXITING_METHOD_EXECUTION);
            logger.debug("Updating {} request status", id);
            return new CaretakerResponse(Constants.SUCCESS, Constants.STATUS_UPDATED, uc.get());
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Update Status" + Constants.SQL_ERROR_MSG);
            throw new UserCaretakerException(Constants.SQL_ERROR_MSG);
        }
    }

    /**
     * @Deprecated This method is not in use
     */
    @Override
    public CaretakerResponse1 getCaretakerRequestStatus(String userId)  {
        logger.info(Constants.STARTING_METHOD_EXECUTION);

        try {
            logger.info(EXITING_METHOD_EXECUTION);
            return new CaretakerResponse1(Constants.SUCCESS,
                    Constants.STATUS_UPDATED,
                    userCaretakerRepository.getCaretakerRequestStatus(userId));
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get caretaker request status" + Constants.SQL_ERROR_MSG);

            throw new UserCaretakerException(Constants.SQL_ERROR_MSG);
        }
    }

    /**
     * This method fetches all the caretaker request to a patient
     */
    @Override
    public CaretakerResponsePage getCaretakerRequestsForPatient(String userId, int page, int limit)
             {

        logger.info(Constants.STARTING_METHOD_EXECUTION);
        Pageable pageableRequest = PageRequest.of(page, limit);

        try {
            Page<UserCaretaker> userCaretaker = userCaretakerRepository.getCaretakerRequestsForPatients(userId, pageableRequest);

            if (userCaretaker.isEmpty()) {
                logger.error("Get Caretaker Requests for patient: Data not found");

                throw new UserCaretakerException(MSG);
            }
            logger.info(EXITING_METHOD_EXECUTION);
            logger.debug("Fetching {} request for patients",userId);
            return new CaretakerResponsePage(Constants.SUCCESS,
                    Constants.STATUS_UPDATED,
                    userCaretaker.getTotalElements(),
                    userCaretaker.getTotalPages(),
                    page,
                    userCaretaker.getContent());
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get caretaker request for patients" + Constants.SQL_ERROR_MSG);

            throw new UserCaretakerException(Constants.SQL_ERROR_MSG);
        }
    }

    /**
     * This method fetches all the caretakers for a patient
     */
    @Override
    @Cacheable(value = "caretakerCache",key = "#userId")
    public CaretakerResponsePage getMyCaretakers(String userId, int page, int limit)  {

        logger.info(Constants.STARTING_METHOD_EXECUTION);
        Pageable pageableRequest = PageRequest.of(page, limit);

        try {
            Page<UserCaretaker> userCaretaker = userCaretakerRepository.getMyCaretakers(userId, pageableRequest);

            if (userCaretaker.isEmpty()) {
                logger.error("Get Caretakers: Data not found");

                throw new UserCaretakerException(MSG);
            }
            logger.info(EXITING_METHOD_EXECUTION);
            logger.debug("Fetching {} caretakers",userId);
            return new CaretakerResponsePage(Constants.SUCCESS,
                    Constants.STATUS_UPDATED,
                    userCaretaker.getTotalElements(),
                    userCaretaker.getTotalPages(),
                    page,
                    userCaretaker.getContent());
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get caretakers" + Constants.SQL_ERROR_MSG);
            throw new UserCaretakerException(Constants.SQL_ERROR_MSG);
        }
    }

    /**
     * This method fetches all the patient request to a caretaker
     */
    @Override
    @Cacheable(value = "caretakerCache",key = "#userId")
    public CaretakerResponsePage getPatientRequests(String userId, int page, int limit)
             {

        logger.info(Constants.STARTING_METHOD_EXECUTION);
        Pageable pageableRequest = PageRequest.of(page, limit);

        try {
            Page<UserCaretaker> userCaretaker = userCaretakerRepository.getPatientRequests(userId, pageableRequest);

            if (userCaretaker.isEmpty()) {
                logger.error("Get Patients Request: Data not found");

                throw new UserCaretakerException(MSG);
            }
            logger.info(EXITING_METHOD_EXECUTION);
            logger.debug("Fetching patients {} request",userId);
            return new CaretakerResponsePage(Constants.SUCCESS,
                    Constants.STATUS_UPDATED,
                    userCaretaker.getTotalElements(),
                    userCaretaker.getTotalPages(),
                    page,
                    userCaretaker.getContent());
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get patients requests" + Constants.SQL_ERROR_MSG);
            throw new UserCaretakerException(Constants.SQL_ERROR_MSG);
        }
    }

    /**
     * This method fetches all the patients under a caretaker
     */
    @Override
    @Cacheable(value = "caretakerCache",key = "#userId")
    public CaretakerResponsePage getPatientsUnderMe(String userId, int page, int limit)
             {

        logger.info(Constants.STARTING_METHOD_EXECUTION);
        Pageable pageableRequest = PageRequest.of(page, limit);

        try {
            Page<UserCaretaker> userCaretaker = userCaretakerRepository.getPatientsUnderMe(userId, pageableRequest);

            if (userCaretaker.isEmpty()) {
                logger.error("Get my patients: Data not found");

                throw new UserCaretakerException(MSG);
            }
            logger.info(EXITING_METHOD_EXECUTION);
            logger.debug("Fetching {} patients",userId);
            return new CaretakerResponsePage(Constants.SUCCESS,
                    Constants.STATUS_UPDATED,
                    userCaretaker.getTotalElements(),
                    userCaretaker.getTotalPages(),
                    page,
                    userCaretaker.getContent());
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get patients" + Constants.SQL_ERROR_MSG);
            throw new UserCaretakerException(Constants.SQL_ERROR_MSG);
        }
    }

    private UserCaretaker mapToEntity(UserCaretakerDTO userCaretakerDTO) {
        return mapper.map(userCaretakerDTO, UserCaretaker.class);
    }
}
