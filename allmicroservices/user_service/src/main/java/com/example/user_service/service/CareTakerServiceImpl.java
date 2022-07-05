package com.example.user_service.service;


import com.example.user_service.exception.DataAccessExceptionMessage;
import com.example.user_service.exception.UserCaretakerException;
import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.model.Image;
import com.example.user_service.model.UserCaretaker;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.Notificationmessage;
import com.example.user_service.pojos.dto.UserCaretakerDTO;
import com.example.user_service.pojos.response.CaretakerResponse;
import com.example.user_service.pojos.response.CaretakerResponse1;
import com.example.user_service.pojos.response.CaretakerResponsePage;
import com.example.user_service.pojos.response.SendImageResponse;
import com.example.user_service.repository.ImageRepository;
import com.example.user_service.repository.UserCaretakerRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.util.Datehelper;
import com.example.user_service.util.Messages;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Optional;

@Service
public class CareTakerServiceImpl implements CareTakerService {

    @Autowired
    private UserCaretakerRepository userCaretakerRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserMedicineRepository userMedicineRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${project.rabbitmq.routingkey2}")
    private String routingKey2;

    @Value("${project.rabbitmq.exchange}")
    private String topicExchange;

    Logger logger =  LoggerFactory.getLogger(CareTakerServiceImpl.class);

    @Override
    public CaretakerResponse saveCareTaker(@Valid UserCaretakerDTO userCaretakerDTO, BindingResult bindingResult) throws UserCaretakerException , UserExceptions {

        logger.info("Save Caretaker");
        if(bindingResult.hasErrors()){
            return new CaretakerResponse() ;
        }
        try {
            UserCaretaker userCaretaker = mapToEntity(userCaretakerDTO);
            userCaretaker.setCreatedAt(Datehelper.getcurrentdatatime());
            if (userCaretakerRepository.check(userCaretaker.getPatientId(), userCaretaker.getCaretakerId()) != null) {
                logger.error("Save Caretaker: Caretaker already present");
                throw new UserCaretakerException(Messages.ALREADY_PRESENT);
            } else {
                return new CaretakerResponse(Messages.SUCCESS,Messages.REQ_SENT_SUCCESS,userCaretakerRepository.save(userCaretaker));
            }
        } catch (DataAccessException dataAccessException) {
            logger.error("Caretaker :" + Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }
    }

    @Override
    public CaretakerResponse updateCaretakerStatus(String cId,BindingResult bindingResult) throws UserCaretakerException, UserExceptions {
        logger.info("Update request status");
        try {
            Optional<UserCaretaker> uc = userCaretakerRepository.findById(cId);
            if (uc.isEmpty()) {
                logger.error("Update Caretaker Status: Data not found");
                throw new UserCaretakerException(Messages.MSG);
            }
            uc.get().setReqStatus(true);
            return new CaretakerResponse(Messages.SUCCESS,Messages.STATUS_UPDATED,userCaretakerRepository.save(uc.get()));
        } catch (DataAccessException dataAccessException) {
            logger.error("Update Status" + Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }
    }

    @Override
    public CaretakerResponsePage getPatientsUnderMe(String userId, int page, int limit) throws UserCaretakerException , UserExceptions{
        logger.info("Get my patients");
        Pageable pageableRequest = PageRequest.of(page, limit);
        try {
            Page<UserCaretaker> userCaretaker = userCaretakerRepository.getPatientsUnderMe(userId,pageableRequest);
            if (userCaretaker.isEmpty()) {
                logger.error("Get my patients: Data not found");
                throw new UserCaretakerException(Messages.MSG);
            }
            return new CaretakerResponsePage(Messages.SUCCESS,Messages.STATUS_UPDATED,userCaretaker.getTotalElements(), userCaretaker.getTotalPages(), page,userCaretaker.get());
        } catch (DataAccessException dataAccessException) {
            logger.error("Get patients" +Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }
    }

    @Override
    public CaretakerResponsePage getPatientRequests(String userId,int page,int limit) throws UserCaretakerException , UserExceptions{
        logger.info("Get patients ");
        Pageable pageableRequest = PageRequest.of(page, limit);
        try {
           Page<UserCaretaker> userCaretaker = userCaretakerRepository.getPatientRequests(userId,pageableRequest);
            if (userCaretaker.isEmpty()) {
                logger.error("Get Patients Request: Data not found");
                throw new UserCaretakerException(Messages.MSG);
            }
            return new CaretakerResponsePage(Messages.SUCCESS,Messages.STATUS_UPDATED,userCaretaker.getTotalElements(), userCaretaker.getTotalPages(), page,userCaretaker.get());
        } catch (DataAccessException dataAccessException) {
            logger.error("Get patients requests" +Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }
    }

    @Override
    public CaretakerResponsePage getMyCaretakers(String userId,int page, int limit) throws UserCaretakerException , UserExceptions{

        logger.info("Get my caretakers ");
        Pageable pageableRequest = PageRequest.of(page, limit);

        try {
            Page<UserCaretaker> userCaretaker = userCaretakerRepository.getMyCaretakers(userId,pageableRequest);
            if (userCaretaker.isEmpty()) {
                logger.error("Get Caretakers: Data not found");
                throw new UserCaretakerException(Messages.MSG);
            }
            return new CaretakerResponsePage(Messages.SUCCESS,Messages.STATUS_UPDATED,userCaretaker.getTotalElements(), userCaretaker.getTotalPages(), page,userCaretaker.get());
        } catch (DataAccessException dataAccessException) {
            logger.error("Get caretakers" +Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }
    }

    @Override
    public CaretakerResponse1 getCaretakerRequestStatus(String userId) {
        logger.info("Get caretaker request status ");
        try {
            return new CaretakerResponse1(Messages.SUCCESS,Messages.STATUS_UPDATED,userCaretakerRepository.getCaretakerRequestStatus(userId));
        } catch (DataAccessException dataAccessException) {
            logger.error("Get caretaker request status" +Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }
    }


    @Override
    public CaretakerResponsePage getCaretakerRequestsP(String userId,int page,int limit) throws UserCaretakerException , UserExceptions{

        Pageable pageableRequest = PageRequest.of(page, limit);

        try {
            Page<UserCaretaker> userCaretaker = userCaretakerRepository.getCaretakerRequestsP(userId,pageableRequest);
            if (userCaretaker.isEmpty()) {
                logger.error("Get Caretaker Requests for patient: Data not found");
                throw new UserCaretakerException(Messages.MSG);
            }
            return new CaretakerResponsePage(Messages.SUCCESS,Messages.STATUS_UPDATED,userCaretaker.getTotalElements(), userCaretaker.getTotalPages(), page,userCaretaker.get());
        } catch (DataAccessException dataAccessException) {
            logger.error("Get caretaker request for patients"+ Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }
    }

    @Override
    public String delPatientReq(String cId) throws UserExceptionMessage, UserCaretakerException , UserExceptions{

        logger.info("Delete patient request ");
        try {
            Optional<UserCaretaker> userCaretaker = userCaretakerRepository.findById(cId);
            if (userCaretaker.isPresent()) {
                userCaretakerRepository.delete(userCaretaker.get());
                return "Success";

            }
            logger.error("Delete Request: Data not found");
            throw new UserCaretakerException(Messages.MSG);
        } catch (Exception e) {
            logger.error("Delete patient request "+ Messages.MSG);
            throw new UserCaretakerException(Messages.MSG);
        }
    }

    @Override
    public SendImageResponse sendImageToCaretaker(MultipartFile multipartFile, String filename, String caretakerid, String medName, Integer medId) throws IOException, UserCaretakerException , UserExceptions{

        logger.info("Send image to caretaker");
        try {
            File file = new File(System.getProperty("user.dir") + "/src/main/upload/static/images");
            if (!file.exists()) {
                file.mkdir();
            }
            Path path = Paths.get(System.getProperty("user.dir") + "/src/main/upload/static/images", filename.concat(".").concat("jpg"));
            Files.write(path, multipartFile.getBytes());

            UserMedicines userMedicines = userMedicineRepository.getMedById(medId);
            String userName = userMedicines.getUserEntity().getUserName();
            Image image = new Image();
            image.setImageUrl(path.getFileName().toString());
            image.setTime(Calendar.getInstance().getTime().toString());
            image.setDate(Calendar.getInstance().getTime());
            image.setUserMedicines(userMedicines);
            image.setCaretakerName(userName);
            imageRepository.save(image);
            String fcmToken = "epkw4MI-RxyMzZjvD6fUl6:APA91bEUyAJpJ5RmDyI1KLcMLJbBPiYSX64oIW4WkNq62zeUlMPUPknGkBHTB_drOBX6CUkiI0Pyfc4Myvt87v6BU69kz0LPq4YM9iWnG9RrNbxIpC4LrtE-zWfNdbB3dbjR2bmogops";
            rabbitTemplate.convertAndSend(topicExchange, routingKey2, new Notificationmessage(fcmToken, "Take medicine", "caretaker", medName, filename + ".jpg"));

        } catch (Exception e) {
            logger.info(e.getMessage());
            return new SendImageResponse(Messages.FAILED,Messages.UNABLE_TO_SEND);
        }

        return new SendImageResponse(Messages.SUCCESS,Messages.SENT_SUCCESSFULLY);
    }

    private UserCaretaker mapToEntity(UserCaretakerDTO userCaretakerDTO) {
        return mapper.map(userCaretakerDTO, UserCaretaker.class);

    }



}