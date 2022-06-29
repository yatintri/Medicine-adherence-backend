package com.example.user_service.service;

import com.example.user_service.exception.UserExceptionMessage;
import com.example.user_service.exception.UserExceptions;
import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.model.Image;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.dto.MedicineHistoryDTO;
import com.example.user_service.pojos.response.ImageListResponse;
import com.example.user_service.pojos.response.MedicineResponse;
import com.example.user_service.pojos.response.MedicineResponsePage;

import java.util.List;
import java.util.concurrent.CompletableFuture;
//
public interface UserMedicineService {


    CompletableFuture<List<UserMedicines>> getallUserMedicines(String userId,int page,int limit) throws UserMedicineException, UserExceptionMessage, UserExceptions;

    boolean syncData(String userId , List<UserMedicines> list) throws UserMedicineException, UserExceptions;

    MedicineResponse syncMedicineHistory(Integer medId , List<MedicineHistoryDTO> medicineHistoryDTOS) throws UserMedicineException, UserExceptions;

    MedicineResponse getMedicineHistory(Integer medId, int page, int limit) throws UserMedicineException, UserExceptions;

    ImageListResponse getUserMedicineImages(Integer medId, int page, int limit) throws UserExceptions;

}
