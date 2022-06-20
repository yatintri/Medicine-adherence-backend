package com.example.user_service.service;


import com.example.user_service.exception.DataAccessExceptionMessage;
import com.example.user_service.exception.UserCaretakerException;
import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.model.Image;
import com.example.user_service.model.UserCaretaker;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.Notificationmessage;
import com.example.user_service.pojos.dto.UserCaretakerDTO;
import com.example.user_service.repository.ImageRepository;
import com.example.user_service.repository.UserCaretakerRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.util.Datehelper;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
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

    private static final String MSG = "Data not found";
    private static final String errorMsg = "SQL error!";

    @Override
    public UserCaretaker saveCareTaker(UserCaretakerDTO userCaretakerDTO) throws UserCaretakerException {

        try {
            UserCaretaker userCaretaker = mapToEntity(userCaretakerDTO);
            userCaretaker.setCreatedAt(Datehelper.getcurrentdatatime());
            if (userCaretakerRepository.check(userCaretaker.getPatientId(), userCaretaker.getCaretakerId()) != null) {
                throw new UserCaretakerException("Caretaker Already Present!!");
            } else {
                return userCaretakerRepository.save(userCaretaker);
            }
        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }
    }

    @Override
    public UserCaretaker updateCaretakerStatus(String cId) throws UserCaretakerException {
        try {
            Optional<UserCaretaker> uc = userCaretakerRepository.findById(cId);
            if (uc.isEmpty()) {
                throw new UserCaretakerException("Request not found");
            }
            uc.get().setReqStatus(true);
            return userCaretakerRepository.save(uc.get());
        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }
    }

    @Override
    public List<UserCaretaker> getPatientsUnderMe(String userId) throws UserCaretakerException {

        try {
            List<UserCaretaker> userCaretaker = userCaretakerRepository.getPatientsUnderMe(userId);
            if (userCaretaker.isEmpty()) {
                throw new UserCaretakerException(MSG);
            }
            return userCaretaker;
        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }
    }

    @Override
    public List<UserCaretaker> getPatientRequests(String userId) throws UserCaretakerException {
        try {
            List<UserCaretaker> userCaretaker = userCaretakerRepository.getPatientRequests(userId);
            if (userCaretaker.isEmpty()) {
                throw new UserCaretakerException(MSG);
            }
            return userCaretaker;
        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }
    }

    @Override
    public List<UserCaretaker> getMyCaretakers(String userId) throws UserCaretakerException {
        try {
            List<UserCaretaker> userCaretaker = userCaretakerRepository.getMyCaretakers(userId);
            if (userCaretaker.isEmpty()) {
                throw new UserCaretakerException(MSG);
            }
            return userCaretaker;
        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }
    }

    @Override
    public List<UserCaretaker> getCaretakerRequestStatus(String userId) {
        try {
            return userCaretakerRepository.getCaretakerRequestStatus(userId);
        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }
    }


    @Override
    public List<UserCaretaker> getCaretakerRequestsP(String userId) throws UserCaretakerException {
        try {
            List<UserCaretaker> userCaretaker = userCaretakerRepository.getCaretakerRequestsP(userId);
            if (userCaretaker.isEmpty()) {
                throw new UserCaretakerException(MSG);
            }
            return userCaretaker;
        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }
    }

    @Override
    public String delPatientReq(String cId) throws UserExceptionMessage, UserCaretakerException {


        try {
            Optional<UserCaretaker> userCaretaker = userCaretakerRepository.findById(cId);
            if (userCaretaker.isPresent()) {
                userCaretakerRepository.delete(userCaretaker.get());
                return "Success";

            }
            throw new UserCaretakerException("No record found");
        } catch (Exception e) {
            throw new UserCaretakerException("No record found");
        }
    }

    @Override
    public boolean sendImageToCaretaker(MultipartFile multipartFile, String filename, String caretakerid, String medName, Integer medId) throws IOException, UserCaretakerException {

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
            rabbitTemplate.convertAndSend("project_exchange", "notification_key", new Notificationmessage(fcmToken, "Take medicine", "caretaker", medName, filename + ".jpg"));

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private UserCaretaker mapToEntity(UserCaretakerDTO userCaretakerDTO) {
        return mapper.map(userCaretakerDTO, UserCaretaker.class);

    }

///

}