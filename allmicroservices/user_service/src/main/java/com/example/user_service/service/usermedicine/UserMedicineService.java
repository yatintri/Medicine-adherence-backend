package com.example.user_service.service.usermedicine;

import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.dto.MedicineHistoryDTO;
import com.example.user_service.pojos.dto.MedicinePojo;
import com.example.user_service.pojos.response.image.ImageListResponse;
import com.example.user_service.pojos.response.medicine.MedicineResponse;
import com.example.user_service.pojos.response.medicine.SyncResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This is an interface for user medicine service
 */
public interface UserMedicineService {


    CompletableFuture<List<UserMedicines>> getallUserMedicines(String userId,int page,int limit) throws UserMedicineException, UserExceptionMessage, UserExceptions;

    SyncResponse syncData(String userId , List<MedicinePojo> list) throws UserMedicineException, UserExceptions;

    MedicineResponse syncMedicineHistory(Integer medId , List<MedicineHistoryDTO> medicineHistoryDTOS) throws UserMedicineException, UserExceptions;

    MedicineResponse getMedicineHistory(Integer medId, int page, int limit) throws UserMedicineException, UserExceptions;

    ImageListResponse getUserMedicineImages(Integer medId, int page, int limit) throws UserExceptions, UserMedicineException;

}
