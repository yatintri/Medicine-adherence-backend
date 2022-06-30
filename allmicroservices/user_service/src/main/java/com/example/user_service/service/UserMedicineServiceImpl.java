package com.example.user_service.service;

import com.example.user_service.exception.DataAccessExceptionMessage;
import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.model.*;
import com.example.user_service.pojos.dto.MedicineHistoryDTO;
import com.example.user_service.pojos.response.ImageListResponse;
import com.example.user_service.pojos.response.MedicineResponse;
import com.example.user_service.pojos.response.MedicineResponsePage;
import com.example.user_service.repository.ImageRepository;
import com.example.user_service.repository.UserMedHistoryRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.util.Messages;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    UserMedicineRepository userMedicineRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    UserMedHistoryRepository userMedHistoryRepository;

    Logger logger = LoggerFactory.getLogger(UserMedicineServiceImpl.class);

    @Override
    @Async
    public CompletableFuture<List<UserMedicines>> getallUserMedicines(String userId,int page,int limit) throws UserMedicineException, UserExceptionMessage, UserExceptions {

//        Pageable pageableRequest = PageRequest.of(page, limit);
//        Page<UserMedicines> users = userMedicineRepository.findAll(pageableRequest);
//        List<UserMedicines> userEntities = users.getContent();

        try {
            UserEntity user = userRepository.getUserById(userId);
            if (user == null) {
                throw new UserExceptionMessage(Messages.PROVIDE_VALID_ID);
            }
            List<UserMedicines> list = user.getUserMedicines().subList(0,2);

            return CompletableFuture.completedFuture(list);
        } catch (DataAccessException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }

    }


    @Override
    public boolean syncData(String userId, List<UserMedicines> list) throws UserMedicineException, UserExceptions {
        try {
            UserEntity user = userRepository.getUserById(userId);
            if (user.getUserMedicines().isEmpty()) {
                throw new UserMedicineException(Messages.UNABLE_TO_SYNC);
            }
            for (UserMedicines userMedicines : list) {

                userMedicines.setUserEntity(user);

            }

            return false;
        } catch (DataAccessException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }
    }


    @Override

    public MedicineResponse syncMedicineHistory(Integer medId, List<MedicineHistoryDTO> medicineHistoryDTOS) throws UserMedicineException, UserExceptions {
        try {
            UserMedicines userMedicines = userMedicineRepository.getMedById(medId);
            if (userMedicines == null) {
                throw new UserMedicineException(Messages.UNABLE_TO_SYNC);

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
            logger.error(Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }
    }

    @Override
    public MedicineResponse getMedicineHistory(Integer medId, int page, int limit) throws UserMedicineException, UserExceptions {

        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<UserMedicines> users = userMedicineRepository.findAll(pageableRequest);
        List<UserMedicines> userEntities = users.getContent();

        try {
            List<MedicineHistory> medicineHistories = userMedicineRepository.getMedById(medId).getMedicineHistories().subList(0,2);
            if (medicineHistories.isEmpty()) {
                throw new UserMedicineException(Messages.MSG);
            }
            return new MedicineResponse("OK", "Medicine History",medicineHistories);
        } catch (DataAccessException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }
        catch (NullPointerException exception){
            throw new UserMedicineException(Messages.NO_MEDICINE_FOUND);
        }
    }

    @Override
    public ImageListResponse getUserMedicineImages(Integer medId, int page, int limit)throws UserExceptions {

        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<UserMedicines> users = userMedicineRepository.findAll(pageableRequest);
        List<UserMedicines> userEntities = users.getContent();

        try {
            return new ImageListResponse("","",userMedicineRepository.getMedById(medId)
                    .getImages()
                    .stream()
                    .sorted(Comparator.comparing(Image::getDate).reversed())
                    .collect(Collectors.toList()));

        } catch (DataAccessException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);
            throw new DataAccessExceptionMessage(Messages.SQL_ERROR_MSG + dataAccessException.getMessage());
        }
    }

}
