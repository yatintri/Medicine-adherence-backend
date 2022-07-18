package com.example.user_service.service.caretaker;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

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
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.model.Image;
import com.example.user_service.model.UserCaretaker;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.Notificationmessage;
import com.example.user_service.pojos.dto.UserCaretakerDTO;
import com.example.user_service.pojos.response.caretaker.CaretakerResponse;
import com.example.user_service.pojos.response.caretaker.CaretakerResponse1;
import com.example.user_service.pojos.response.caretaker.CaretakerResponsePage;
import com.example.user_service.pojos.response.image.SendImageResponse;
import com.example.user_service.repository.ImageRepository;
import com.example.user_service.repository.UserCaretakerRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.util.Datehelper;
import com.example.user_service.util.Messages;

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
    @Value("${project.rabbitmq.routingkey2}")
    private String routingKey2;
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
    @CacheEvict(value = "caretakerCache")
    public String delPatientReq(String cId) throws UserExceptionMessage, UserCaretakerException, UserExceptions {
        logger.info(Messages.STARTING_METHOD_EXECUTION);

        try {
            Optional<UserCaretaker> userCaretaker = userCaretakerRepository.findById(cId);

            if (userCaretaker.isPresent()) {
                userCaretakerRepository.delete(userCaretaker.get());

                return "Success";
            }

            logger.info(Messages.EXITING_METHOD_EXECUTION);
            logger.debug("Deleted {} request",cId);

            throw new UserCaretakerException(Messages.MSG);
        } catch (Exception e) {
            logger.error("Delete patient request " + Messages.MSG);
            throw new UserCaretakerException(Messages.MSG);
        }
    }

    /**
     * This method contains the logic to save a caretaker
     */
    @Override
    public CaretakerResponse saveCareTaker(@Valid UserCaretakerDTO userCaretakerDTO)
            throws UserCaretakerException, UserExceptionMessage {
        logger.info(Messages.STARTING_METHOD_EXECUTION);

        try {
            UserCaretaker userCaretaker = mapToEntity(userCaretakerDTO);

            userCaretaker.setCreatedAt(Datehelper.getcurrentdatatime());

            if (userCaretakerRepository.check(userCaretaker.getPatientId(), userCaretaker.getCaretakerId()) != null) {

                logger.error("Save Caretaker: Caretaker already present");
                throw new UserCaretakerException(Messages.ALREADY_PRESENT);

            } else {
                userCaretakerRepository.save(userCaretaker);
                logger.info(Messages.EXITING_METHOD_EXECUTION);
                logger.debug("Saving {} caretaker",userCaretakerDTO);
                return new CaretakerResponse(Messages.SUCCESS, Messages.REQ_SENT_SUCCESS, userCaretaker);
            }
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {

            logger.error("Caretaker :" + Messages.SQL_ERROR_MSG);
            throw new UserExceptionMessage(Messages.SQL_ERROR_MSG);
        }
    }

    /**
     * This method uses fcm to send image to caretaker as a notification message
     */
    @Override
    public SendImageResponse sendImageToCaretaker(MultipartFile multipartFile, String filename, String medName,
                                                  String caretakerid, Integer medId)
            throws IOException, UserCaretakerException, UserExceptions {
        logger.info(Messages.STARTING_METHOD_EXECUTION);

        try {
            Path path = Paths.get(System.getProperty("user.dir") + "/src/main/upload/static/images",
                    filename.concat(".").concat("jpg"));

            Files.write(path, multipartFile.getBytes());

            UserMedicines userMedicines = userMedicineRepository.getMedById(medId);
            if(userMedicines == null){
                throw new UserCaretakerException(Messages.NO_MEDICINE_FOUND);

            }
            String userName = userMedicines.getUserEntity().getUserName();
            Image image = new Image();

            image.setImageUrl(path.getFileName().toString());
            image.setTime(Calendar.getInstance().getTime().toString());
            image.setDate(Calendar.getInstance().getTime());
            image.setUserMedicines(userMedicines);
            image.setCaretakerName(userName);
            imageRepository.save(image);

            String fcmToken =
                    "epkw4MI-RxyMzZjvD6fUl6:APA91bEUyAJpJ5RmDyI1KLcMLJbBPiYSX64oIW4WkNq62zeUlMPUPknGkBHTB_drOBX6CUkiI0Pyfc4Myvt87v6BU69kz0LPq4YM9iWnG9RrNbxIpC4LrtE-zWfNdbB3dbjR2bmogops";

            rabbitTemplate.convertAndSend(topicExchange,
                    routingKey2,
                    new Notificationmessage(fcmToken,
                            "Take medicine",
                            "caretaker",
                            medName,
                            filename + ".jpg"));
        } catch (Exception e) {
            logger.info(e.getMessage());

            return new SendImageResponse(Messages.FAILED, Messages.UNABLE_TO_SEND);
        }
        logger.info(Messages.EXITING_METHOD_EXECUTION);
        logger.debug("Sending image {} to {} caretaker for {} medicine",filename,caretakerid,medId);
        return new SendImageResponse(Messages.SUCCESS, Messages.SENT_SUCCESSFULLY);
    }

    /**
     * This method contains logic which updates caretaker request status
     */
    @Override
    @CachePut(value = "caretakerCache")
    public CaretakerResponse updateCaretakerStatus(String cId) throws UserCaretakerException {
        logger.info(Messages.STARTING_METHOD_EXECUTION);

        try {
            Optional<UserCaretaker> uc = userCaretakerRepository.findById(cId);

            if (uc.isEmpty() || Objects.isNull(uc.get().getCId())) {
                logger.error("Update Caretaker Status: Data not found");

                throw new UserCaretakerException(Messages.MSG);
            }

            uc.get().setReqStatus(true);
            userCaretakerRepository.save(uc.get());
            logger.info(Messages.EXITING_METHOD_EXECUTION);
            logger.debug("Updating {} request status", cId);
            return new CaretakerResponse(Messages.SUCCESS, Messages.STATUS_UPDATED, uc.get());
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Update Status" + Messages.SQL_ERROR_MSG);
            throw new UserCaretakerException(Messages.SQL_ERROR_MSG);
        }
    }

    /**
     * @Deprecated This method is not in use
     */
    @Override
    public CaretakerResponse1 getCaretakerRequestStatus(String userId) throws UserCaretakerException {
        logger.info(Messages.STARTING_METHOD_EXECUTION);

        try {
            logger.info(Messages.EXITING_METHOD_EXECUTION);
            return new CaretakerResponse1(Messages.SUCCESS,
                    Messages.STATUS_UPDATED,
                    userCaretakerRepository.getCaretakerRequestStatus(userId));
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get caretaker request status" + Messages.SQL_ERROR_MSG);

            throw new UserCaretakerException(Messages.SQL_ERROR_MSG);
        }
    }

    /**
     * This method fetches all the caretaker request to a patient
     */
    @Override
    public CaretakerResponsePage getCaretakerRequestsP(String userId, int page, int limit)
            throws UserCaretakerException, UserExceptions {

        logger.info(Messages.STARTING_METHOD_EXECUTION);
        Pageable pageableRequest = PageRequest.of(page, limit);

        try {
            Page<UserCaretaker> userCaretaker = userCaretakerRepository.getCaretakerRequestsP(userId, pageableRequest);

            if (userCaretaker.isEmpty()) {
                logger.error("Get Caretaker Requests for patient: Data not found");

                throw new UserCaretakerException(Messages.MSG);
            }
            logger.info(Messages.EXITING_METHOD_EXECUTION);
            logger.debug("Fetching {} request for patients",userId);
            return new CaretakerResponsePage(Messages.SUCCESS,
                    Messages.STATUS_UPDATED,
                    userCaretaker.getTotalElements(),
                    userCaretaker.getTotalPages(),
                    page,
                    userCaretaker.getContent());
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get caretaker request for patients" + Messages.SQL_ERROR_MSG);

            throw new UserCaretakerException(Messages.SQL_ERROR_MSG);
        }
    }

    /**
     * This method fetches all the caretakers for a patient
     */
    @Override
    @Cacheable(value = "caretakerCache")
    public CaretakerResponsePage getMyCaretakers(String userId, int page, int limit) throws UserCaretakerException {

        logger.info(Messages.STARTING_METHOD_EXECUTION);
        Pageable pageableRequest = PageRequest.of(page, limit);

        try {
            Page<UserCaretaker> userCaretaker = userCaretakerRepository.getMyCaretakers(userId, pageableRequest);

            if (userCaretaker.isEmpty()) {
                logger.error("Get Caretakers: Data not found");

                throw new UserCaretakerException(Messages.MSG);
            }
            logger.info(Messages.EXITING_METHOD_EXECUTION);
            logger.debug("Fetching {} caretakers",userId);
            return new CaretakerResponsePage(Messages.SUCCESS,
                    Messages.STATUS_UPDATED,
                    userCaretaker.getTotalElements(),
                    userCaretaker.getTotalPages(),
                    page,
                    userCaretaker.getContent());
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get caretakers" + Messages.SQL_ERROR_MSG);
            throw new UserCaretakerException(Messages.SQL_ERROR_MSG);
        }
    }

    /**
     * This method fetches all the patient request to a caretaker
     */
    @Override
    @Cacheable(value = "caretakerCache")
    public CaretakerResponsePage getPatientRequests(String userId, int page, int limit)
            throws UserCaretakerException, UserExceptions {

        logger.info(Messages.STARTING_METHOD_EXECUTION);
        Pageable pageableRequest = PageRequest.of(page, limit);

        try {
            Page<UserCaretaker> userCaretaker = userCaretakerRepository.getPatientRequests(userId, pageableRequest);

            if (userCaretaker.isEmpty()) {
                logger.error("Get Patients Request: Data not found");

                throw new UserCaretakerException(Messages.MSG);
            }
            logger.info(Messages.EXITING_METHOD_EXECUTION);
            logger.debug("Fetching patients {} request",userId);
            return new CaretakerResponsePage(Messages.SUCCESS,
                    Messages.STATUS_UPDATED,
                    userCaretaker.getTotalElements(),
                    userCaretaker.getTotalPages(),
                    page,
                    userCaretaker.getContent());
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get patients requests" + Messages.SQL_ERROR_MSG);
            throw new UserCaretakerException(Messages.SQL_ERROR_MSG);
        }
    }

    /**
     * This method fetches all the patients under a caretaker
     */
    @Override
    @Cacheable(value = "caretakerCache")
    public CaretakerResponsePage getPatientsUnderMe(String userId, int page, int limit)
            throws UserCaretakerException, UserExceptions {

        logger.info(Messages.STARTING_METHOD_EXECUTION);
        Pageable pageableRequest = PageRequest.of(page, limit);

        try {
            Page<UserCaretaker> userCaretaker = userCaretakerRepository.getPatientsUnderMe(userId, pageableRequest);

            if (userCaretaker.isEmpty()) {
                logger.error("Get my patients: Data not found");

                throw new UserCaretakerException(Messages.MSG);
            }
            logger.info(Messages.EXITING_METHOD_EXECUTION);
            logger.debug("Fetching {} patients",userId);
            return new CaretakerResponsePage(Messages.SUCCESS,
                    Messages.STATUS_UPDATED,
                    userCaretaker.getTotalElements(),
                    userCaretaker.getTotalPages(),
                    page,
                    userCaretaker.getContent());
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error("Get patients" + Messages.SQL_ERROR_MSG);
            throw new UserCaretakerException(Messages.SQL_ERROR_MSG);
        }
    }

    private UserCaretaker mapToEntity(UserCaretakerDTO userCaretakerDTO) {
        return mapper.map(userCaretakerDTO, UserCaretaker.class);
    }
}
