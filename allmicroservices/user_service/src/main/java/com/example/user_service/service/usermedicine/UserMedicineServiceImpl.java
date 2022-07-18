package com.example.user_service.service.usermedicine;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.hibernate.exception.JDBCConnectionException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.model.*;
import com.example.user_service.pojos.dto.MedicineHistoryDTO;
import com.example.user_service.pojos.dto.MedicinePojo;
import com.example.user_service.pojos.response.image.ImageListResponse;
import com.example.user_service.pojos.response.medicine.MedicineResponse;
import com.example.user_service.pojos.response.medicine.SyncResponse;
import com.example.user_service.repository.ImageRepository;
import com.example.user_service.repository.UserMedHistoryRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.util.Messages;

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
            throws UserMedicineException, UserExceptionMessage {
        logger.info(Messages.STARTING_METHOD_EXECUTION);

        try {
            UserEntity user = userRepository.getUserById(userId);

            if (user == null) {
                logger.error("Get user medicines: Provide valid id");

                throw new UserExceptionMessage(Messages.PROVIDE_VALID_ID);
            }

            List<UserMedicines> list = user.getUserMedicines().subList(0, 1);
            logger.debug("Fetching all {} user medicines",userId);
            logger.info(Messages.EXITING_METHOD_EXECUTION);
            return CompletableFuture.completedFuture(list);
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);
            throw new UserMedicineException(Messages.SQL_ERROR_MSG);
        }
    }

    /**
     * This method contains logic to sync data from local storage to backend
     */
    @Override
    public SyncResponse syncData(String userId, List<MedicinePojo> medicinePojo)
            throws UserMedicineException, UserExceptions {
        logger.info(Messages.STARTING_METHOD_EXECUTION);

        try {
            UserEntity userEntity = userRepository.getUserById(userId);

            if (userEntity.getUserMedicines().isEmpty()) {
                logger.error("Sync data:Unable to sync");

                throw new UserMedicineException(Messages.UNABLE_TO_SYNC);
            }

            List<UserMedicines> userMedicinesList = medicinePojo.stream()
                    .map(
                            medicinePojo1 -> {
                                UserMedicines userMedicines =
                                        new UserMedicines();

                                userMedicines.setMedicineDes(
                                        medicinePojo1.getMedicineDes());
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
                                userMedicines.setTotalMedReminders(
                                        medicinePojo1.getTotalMedReminders());
                                userMedicines.setStartDate(
                                        medicinePojo1.getStartDate());
                                userMedicines.setTime(medicinePojo1.getTime());
                                userMedicines.setUserEntity(userEntity);

                                return userMedicines;
                            })
                    .collect(Collectors.toList());

            userMedicineRepository.saveAll(userMedicinesList);
            logger.debug("Syncing {} user {} medicine data",userId,medicinePojo);
            logger.info(Messages.EXITING_METHOD_EXECUTION);
            return new SyncResponse(Messages.SUCCESS, Messages.SYNC);
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);
            throw new UserMedicineException(Messages.SQL_ERROR_MSG);
        }
    }

    /**
     * This method contains logic to sync medicine history from local storage to backend
     */
    @Override
    public MedicineResponse syncMedicineHistory(Integer medId, List<MedicineHistoryDTO> medicineHistoryDTOS)
            throws UserMedicineException, UserExceptions {
        logger.info(Messages.STARTING_METHOD_EXECUTION);

        try {
            UserMedicines userMedicines = userMedicineRepository.getMedById(medId);

            if (userMedicines == null) {
                logger.error("Sync medicine History: Unable to sync");

                throw new UserMedicineException(Messages.UNABLE_TO_SYNC);
            }

            List<MedicineHistory> medicineHistories = medicineHistoryDTOS.stream()
                    .map(
                            medHid -> {
                                MedicineHistory medicineHistory1 =
                                        new MedicineHistory();

                                medicineHistory1.setHistoryId(
                                        medHid.getRemId());
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
            logger.debug("Syncing {} medicine {} histories",medId,medicineHistoryDTOS);
            logger.info(Messages.EXITING_METHOD_EXECUTION);
            return new MedicineResponse(Messages.SUCCESS, Messages.DATA_FOUND, medicineHistories);
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);

            throw new UserMedicineException(Messages.SQL_ERROR_MSG);
        }
    }

    /**
     * This method contains logic to fetch all the medicine history for a particular user
     */
    @Override
    @Cacheable(value = "medicineCache")
    public MedicineResponse getMedicineHistory(Integer medId, int page, int limit) throws UserMedicineException {
        logger.info(Messages.STARTING_METHOD_EXECUTION);

        try {
            List<MedicineHistory> medicineHistories = userMedicineRepository.getMedById(medId).getMedicineHistories();

            if (medicineHistories.isEmpty()) {
                logger.error("Get Medicine history: Data not found");

                throw new UserMedicineException(Messages.MSG);
            }
            logger.debug("Fetching {} medicine history",medId);
            logger.info(Messages.EXITING_METHOD_EXECUTION);
            return new MedicineResponse("OK", "Medicine History", medicineHistories);
        }
        catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);
            throw new UserMedicineException(Messages.SQL_ERROR_MSG);
        }
    }

    /**
     * This method contains logic to fetch all the medicine images for a particular user
     */
    @Override
    @Cacheable(value = "medicineCache")
    public ImageListResponse getUserMedicineImages(Integer medId, int page, int limit)
            throws UserExceptions, UserMedicineException {
        logger.info(Messages.STARTING_METHOD_EXECUTION);

        try {
            logger.debug("Fetching images for {} medicines",medId);
            logger.info(Messages.EXITING_METHOD_EXECUTION);
            return new ImageListResponse("",
                    "",
                    userMedicineRepository.getMedById(medId).getImages().stream().sorted(
                            Comparator.comparing(Image::getDate).reversed()).collect(
                            Collectors.toList()));
        }
            catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);
            throw new UserMedicineException(Messages.SQL_ERROR_MSG);
        }
    }
}


