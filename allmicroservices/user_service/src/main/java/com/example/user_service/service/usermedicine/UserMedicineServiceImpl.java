package com.example.user_service.service.usermedicine;

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
import org.hibernate.exception.JDBCConnectionException;
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

/**
 * This class contains all the business logic for the medicine controller
 */
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

    public UserMedicineServiceImpl(UserRepository userRepository, UserMedicineRepository userMedicineRepository, ImageRepository imageRepository, UserMedHistoryRepository userMedHistoryRepository) {
        this.userRepository=userRepository;
        this.userMedicineRepository=userMedicineRepository;
        this.imageRepository=imageRepository;
        this.userMedHistoryRepository=userMedHistoryRepository;
    }

    /**
     * This method contains logic to fetch all the medicine for a particular user
     */
    @Override
    @Async
    public CompletableFuture<List<UserMedicines>> getallUserMedicines(String userId,int page,int limit) throws UserMedicineException, UserExceptionMessage {

        logger.info("Get all user medicines ");

        try {
            UserEntity user = userRepository.getUserById(userId);
            if (user == null) {
                logger.error("Get user medicines: Provide valid id");
                throw new UserExceptionMessage(Messages.PROVIDE_VALID_ID);
            }
            List<UserMedicines> list = user.getUserMedicines().subList(0,1);

            return CompletableFuture.completedFuture(list);
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);
            throw new UserMedicineException(Messages.SQL_ERROR_MSG );
        }

    }

    /**
     * This method contains logic to sync data from local storage to backend
     */
    @Override
    public SyncResponse syncData(String userId, List<MedicinePojo> medicinePojo) throws UserMedicineException, UserExceptions {

        logger.info("Sync Data ");
        try {
            UserEntity userEntity = userRepository.getUserById(userId);
            if (userEntity.getUserMedicines().isEmpty()) {
                logger.error("Sync data:Unable to sync");
                throw new UserMedicineException(Messages.UNABLE_TO_SYNC);
            }
                List<UserMedicines> userMedicinesList = medicinePojo.stream().map(medicinePojo1 -> {
                            UserMedicines userMedicines = new UserMedicines();

                            userMedicines.setMedicineDes(medicinePojo1.getMedicineDes());
                            userMedicines.setMedicineName(medicinePojo1.getMedicineName());
                            userMedicines.setDays(medicinePojo1.getDays());
                            userMedicines.setMedicineId(medicinePojo1.getUserId());
                            userMedicines.setEndDate(medicinePojo1.getEndDate());
                            userMedicines.setTitle(medicinePojo1.getTitle());
                            userMedicines.setCurrentCount(medicinePojo1.getCurrentCount());
                            userMedicines.setTotalMedReminders(medicinePojo1.getTotalMedReminders());
                            userMedicines.setStartDate(medicinePojo1.getStartDate());
                            userMedicines.setTime(medicinePojo1.getTime());
                            userMedicines.setUserEntity(userEntity);

                            return userMedicines;
                        })
                        .collect(Collectors.toList());

                userMedicineRepository.saveAll(userMedicinesList);
            return  new SyncResponse(Messages.SUCCESS,Messages.SYNC);

        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);
            throw new UserMedicineException(Messages.SQL_ERROR_MSG );
            }


    }

    /**
     * This method contains logic to sync medicine history from local storage to backend
     */
    @Override
    public MedicineResponse syncMedicineHistory(Integer medId, List<MedicineHistoryDTO> medicineHistoryDTOS) throws UserMedicineException, UserExceptions {

        logger.info("Sync medicine history ");
        try {
            UserMedicines userMedicines = userMedicineRepository.getMedById(medId);
            if (userMedicines == null) {
                logger.error("Sync medicine History: Unable to sync");
                throw new UserMedicineException(Messages.UNABLE_TO_SYNC);

            }

            List<MedicineHistory> medicineHistories = medicineHistoryDTOS.stream().map(medHid -> {
                MedicineHistory medicineHistory1 = new MedicineHistory();
                medicineHistory1.setHistoryId(medHid.getRemId());
                medicineHistory1.setDate(medHid.getDate());
                medicineHistory1.setTaken(String.join(",", medHid.getTaken()));
                medicineHistory1.setNotTaken(String.join(",", medHid.getNotTaken()));
                medicineHistory1.setUserMedicines(userMedicines);
                return medicineHistory1;
            }).collect(Collectors.toList());
            CompletableFuture.completedFuture(userMedHistoryRepository.saveAll(medicineHistories));

            return new MedicineResponse(Messages.SUCCESS,Messages.DATA_FOUND,medicineHistories);
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);
            throw new UserMedicineException(Messages.SQL_ERROR_MSG);
        }
    }
    /**
     * This method contains logic to fetch all the medicine history for a particular user
     */
    @Override
    public MedicineResponse getMedicineHistory(Integer medId, int page, int limit) throws UserMedicineException {

        logger.info("Get medicine history ");
        try {
            List<MedicineHistory> medicineHistories = userMedicineRepository.getMedById(medId).getMedicineHistories();
            if (medicineHistories.isEmpty()) {
                logger.error("Get Medicine history: Data not found");
                throw new UserMedicineException(Messages.MSG);
            }
            return new MedicineResponse("OK", "Medicine History",medicineHistories);
        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);
            throw new UserMedicineException(Messages.SQL_ERROR_MSG);
        }

    }

    /**
     * This method contains logic to fetch all the medicine images for a particular user
     */
    @Override
    public ImageListResponse getUserMedicineImages(Integer medId, int page, int limit) throws UserExceptions, UserMedicineException {

        logger.info("Get user medicine images");
        try {
            return new ImageListResponse("","",userMedicineRepository.getMedById(medId)
                    .getImages()
                    .stream()
                    .sorted(Comparator.comparing(Image::getDate).reversed())
                    .collect(Collectors.toList()));

        } catch (DataAccessException | JDBCConnectionException dataAccessException) {
            logger.error(Messages.SQL_ERROR_MSG);
            throw new UserMedicineException(Messages.SQL_ERROR_MSG );
        }
    }

}
