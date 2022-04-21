package com.example.user_service.service;

import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.dto.MedicineHistoryDTO;
import com.example.user_service.pojos.dto.Medicinepojo;
import com.example.user_service.pojos.response.MedicineResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;
//
public interface UserMedicineService {


    public CompletableFuture<List<UserMedicines>> getallUserMedicines(String userId) throws UserMedicineException, UserexceptionMessage;

    boolean syncData(String userId , List<UserMedicines> list) throws UserMedicineException;

    public MedicineResponse syncMedicineHistory(Integer medId , List<MedicineHistoryDTO> medicineHistoryDTOS) throws UserMedicineException;

    public MedicineResponse getMedicineHistory(Integer medId) throws UserMedicineException;
}
///