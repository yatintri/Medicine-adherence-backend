package com.example.user_service.service;

import com.example.user_service.exception.DataAccessExceptionMessage;
import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.model.Image;
import com.example.user_service.model.MedicineHistory;
import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.dto.MedicineHistoryDTO;
import com.example.user_service.pojos.response.MedicineResponse;
import com.example.user_service.repository.ImageRepository;
import com.example.user_service.repository.UserMedHistoryRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class UserMedicineServiceImpl implements UserMedicineService {

    @Autowired
    UserRepository userRepository;

    public static final String ERROR = "Please enter valid id";
    @Autowired
    UserMedicineRepository userMedicineRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    UserMedHistoryRepository userMedHistoryRepository;

    Logger logger = LoggerFactory.getLogger(UserMedicineServiceImpl.class);

    private static final String errorMsg = "SQL error!";


    @Override
    @Async
    public CompletableFuture<List<UserMedicines>> getallUserMedicines(String userId) throws UserMedicineException, UserExceptionMessage {

        try {
            UserEntity user = userRepository.getUserById(userId);
            if (user == null) {
                throw new UserExceptionMessage(ERROR);
            }
            List<UserMedicines> list = user.getUserMedicines();

            return CompletableFuture.completedFuture(list);
        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }

    }


    @Override
    public boolean syncData(String userId, List<UserMedicines> list) throws UserMedicineException {
        try {
            UserEntity user = userRepository.getUserById(userId);
            if (user.getUserMedicines().isEmpty()) {
                throw new UserMedicineException("Unable to sync");
            }
            for (UserMedicines userMedicines : list) {

                userMedicines.setUserEntity(user);

            }

            return false;
        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }
    }


    @Override

    public MedicineResponse syncMedicineHistory(Integer medId, List<MedicineHistoryDTO> medicineHistoryDTOS) throws UserMedicineException {
        try {
            UserMedicines userMedicines = userMedicineRepository.getMedById(medId);
            if (userMedicines == null) {
                throw new UserMedicineException("Unable to sync");

            }

            List<MedicineHistory> medicineHistories = medicineHistoryDTOS.stream().map(medHid -> {
                MedicineHistory medicineHistory1 = new MedicineHistory();
                medicineHistory1.setHistoryId(medHid.getRemId());
                medicineHistory1.setDate(medHid.getDate());
                medicineHistory1.setTaken(String.join(",", medHid.getTaken()));
                medicineHistory1.setNotTaken(String.join(",", medHid.getNot_taken()));
                medicineHistory1.setUserMedicines(userMedicines);
                return medicineHistory1;
            }).collect(Collectors.toList());
            CompletableFuture.completedFuture(userMedHistoryRepository.saveAll(medicineHistories));

            return null;
        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }
    }

    @Override
    public MedicineResponse getMedicineHistory(Integer medId) throws UserMedicineException {

        try {
            List<MedicineHistory> medicineHistories = userMedicineRepository.getMedById(medId).getMedicineHistories();
            if (medicineHistories.isEmpty()) {
                throw new UserMedicineException("No record found!!");
            }
            return new MedicineResponse("OK", "Medicine History", medicineHistories);
        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }
        catch (NullPointerException exception){
            throw new UserMedicineException("No medicine found");
        }
    }

    @Override
    public List<Image> getUserMedicineImages(Integer medId) {

        try {
            return userMedicineRepository.getMedById(medId)
                    .getImages()
                    .stream()
                    .sorted(Comparator.comparing(Image::getDate).reversed())
                    .collect(Collectors.toList());

        } catch (DataAccessException dataAccessException) {
            throw new DataAccessExceptionMessage(errorMsg + dataAccessException.getMessage());
        }
    }


}
//