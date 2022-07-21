package com.example.user_service.service.impl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.example.user_service.service.UserMedicineService;
import org.hibernate.exception.JDBCConnectionException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.model.*;
import com.example.user_service.pojos.dto.request.MedicineHistoryDTO;
import com.example.user_service.pojos.dto.request.MedicinePojo;
import com.example.user_service.pojos.dto.response.image.ImageListResponse;
import com.example.user_service.pojos.dto.response.medicine.MedicineResponse;
import com.example.user_service.pojos.dto.response.medicine.SyncResponse;
import com.example.user_service.repository.ImageRepository;
import com.example.user_service.repository.UserMedHistoryRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;

import static com.example.user_service.util.Constants.*;

/**
 * This class contains all the business logic for the medicine controller
 */
@Service
public class UserMedicineServiceImpl implements UserMedicineService {
    Logger logger = LoggerFactory.getLogger(UserMedicineServiceImpl.class);

    UserRepository userRepository;

    UserMedicineRepository userMedicineRepository;

    ImageRepository imageRepository;

    UserMedHistoryRepository userMedHistoryRepository;

    public UserMedicineServiceImpl(UserRepository userRepository, UserMedicineRepository userMedicineRepository,
                                   ImageRepository imageRepository, UserMedHistoryRepository userMedHistoryRepository) {
        this.userRepository = userRepository;
        this.userMedicineRepository = userMedicineRepository;
        this.imageRepository = imageRepository;
        this.userMedHistoryRepository = userMedHistoryRepository;
    }

    /**
     * This method contains logic to fetch all the medicine for a particular user
     */
    @Override
    @Async
    @Cacheable(value = "medicineCache")
    public CompletableFuture<List<UserMedicines>> getallUserMedicines(String userId, int page, int limit)
             {
        logger.info(STARTING_METHOD_EXECUTION);

        try {
            User user = userRepository.getUserById(userId);

            if (user == null) {
                logger.error("Get user medicines: Provide valid id");

                throw new UserExceptionMessage(PROVIDE_VALID_ID);
            }

            List<UserMedicines> list = user.getUserMedicines().subList(0, 1);
            logger.debug("Fetching all {} user medicines",userId);
            logger.info(EXITING_METHOD_EXECUTION);
            return CompletableFuture.completedFuture(list);
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(SQL_ERROR_MSG);
            throw new UserMedicineException(SQL_ERROR_MSG);
        }
    }

    /**
     * This method contains logic to sync data from local storage to backend
     */
    @Override
    public SyncResponse syncData(String userId, List<MedicinePojo> medicinePojo)
             {
        logger.info(STARTING_METHOD_EXECUTION);

        try {
            User userEntity = userRepository.getUserById(userId);

            if (userEntity.getUserMedicines().isEmpty()) {
                logger.error("Sync data:Unable to sync");

                throw new UserMedicineException(UNABLE_TO_SYNC);
            }

            List<UserMedicines> userMedicinesList = medicinePojo.stream()
                    .map(
                            medicinePojo1 -> {
                                UserMedicines userMedicines =
                                        new UserMedicines();

                                userMedicines.setCreatedAt(LocalDateTime.now());
                                userMedicines.setUpdatedAt(LocalDateTime.now());
                                userMedicines.setMedicineDescription(
                                        medicinePojo1.getMedicineDescription());
                                userMedicines.setMedicineName(
                                        medicinePojo1.getMedicineName());
                                userMedicines.setDays(medicinePojo1.getDays());
                                userMedicines.setMedicineId(
                                        medicinePojo1.getUserId());
                                userMedicines.setEndDate(
                                        medicinePojo1.getEndDate());
                                userMedicines.setTitle(
                                        medicinePojo1.getTitle());
                                userMedicines.setCurrentCount(
                                        medicinePojo1.getCurrentCount());
                                userMedicines.setTotalMedicineReminders(
                                        medicinePojo1.getTotalMedicineReminders());
                                userMedicines.setStartDate(
                                        medicinePojo1.getStartDate());
                                userMedicines.setTime(medicinePojo1.getTime());
                                userMedicines.setUserEntity(userEntity);

                                return userMedicines;
                            })
                    .collect(Collectors.toList());

            userMedicineRepository.saveAll(userMedicinesList);
            logger.debug("Syncing {} user {} medicine data",userId,medicinePojo);
            logger.info(EXITING_METHOD_EXECUTION);
            return new SyncResponse(SUCCESS, SYNC);
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(SQL_ERROR_MSG);
            throw new UserMedicineException(SQL_ERROR_MSG);
        }
    }

    /**
     * This method contains logic to sync medicine history from local storage to backend
     */
    @Override
    public MedicineResponse syncMedicineHistory(Integer medicineId, List<MedicineHistoryDTO> medicineHistoryDTOS)
            {
        logger.info(STARTING_METHOD_EXECUTION);

        try {
            UserMedicines userMedicines = userMedicineRepository.getMedicineById(medicineId);

            if (userMedicines == null) {
                logger.error("Sync medicine History: Unable to sync");

                throw new UserMedicineException(UNABLE_TO_SYNC);
            }

            List<MedicineHistory> medicineHistories = medicineHistoryDTOS.stream()
                    .map(
                            medHid -> {
                                MedicineHistory medicineHistory1 =
                                        new MedicineHistory();

                                medicineHistory1.setCreatedAt(LocalDateTime.now());
                                medicineHistory1.setUpdatedAt(LocalDateTime.now());
                                medicineHistory1.setHistoryId(
                                        medHid.getReminderId());
                                medicineHistory1.setDate(
                                        medHid.getDate());
                                medicineHistory1.setTaken(
                                        String.join(
                                                ",", medHid.getTaken()));
                                medicineHistory1.setNotTaken(
                                        String.join(
                                                ",", medHid.getNotTaken()));
                                medicineHistory1.setUserMedicines(
                                        userMedicines);

                                return medicineHistory1;
                            })
                    .collect(Collectors.toList());

            CompletableFuture.completedFuture(userMedHistoryRepository.saveAll(medicineHistories));
            logger.debug("Syncing {} medicine {} histories",medicineId,medicineHistoryDTOS);
            logger.info(EXITING_METHOD_EXECUTION);
            return new MedicineResponse(SUCCESS, DATA_FOUND, medicineHistories);
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(SQL_ERROR_MSG);

            throw new UserMedicineException(SQL_ERROR_MSG);
        }
    }

    /**
     * This method contains logic to fetch all the medicine history for a particular user
     */
    @Override
    @Cacheable(value = "medicineCache")
    public MedicineResponse getMedicineHistory(Integer medicineId, int page, int limit) {
        logger.info(STARTING_METHOD_EXECUTION);

        try {
            List<MedicineHistory> medicineHistories = userMedicineRepository.getMedicineById(medicineId).getMedicineHistories();

            if (medicineHistories.isEmpty()) {
                logger.error("Get Medicine history: Data not found");

                throw new UserMedicineException(MSG);
            }
            logger.debug("Fetching {} medicine history",medicineId);
            logger.info(EXITING_METHOD_EXECUTION);
            return new MedicineResponse("OK", "Medicine History", medicineHistories);
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(SQL_ERROR_MSG);
            throw new UserMedicineException(SQL_ERROR_MSG);
        }
    }

    /**
     * This method contains logic to fetch all the medicine images for a particular user
     */
    @Override
    @Cacheable(value = "medicineCache")
    public ImageListResponse getUserMedicineImages(Integer medicineId, int page, int limit)
             {
        logger.info(STARTING_METHOD_EXECUTION);

        try {
            logger.debug("Fetching images for {} medicines",medicineId);
            logger.info(EXITING_METHOD_EXECUTION);
            return new ImageListResponse("",
                    "",
                    userMedicineRepository.getMedicineById(medicineId).getImages().stream().sorted(
                            Comparator.comparing(Image::getDate).reversed()).collect(
                            Collectors.toList()));
        }
            catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(SQL_ERROR_MSG);
            throw new UserMedicineException(SQL_ERROR_MSG);
        }
    }
}


