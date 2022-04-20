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

    public UserMedicines saveUserMedicine(String userId, Medicinepojo medicinepojo)throws UserMedicineException, UserexceptionMessage;

    public boolean updateMedicineStatus(Integer medicineId) throws UserMedicineException;

    public CompletableFuture<List<UserMedicines>> getallUserMedicines(String userId) throws UserMedicineException, UserexceptionMessage;

    public UserMedicines editMedicineDetails(Integer medicineId , Medicinepojo medicinepojo)throws UserMedicineException, UserexceptionMessage;

    boolean syncData(String userId , List<UserMedicines> list) throws UserMedicineException;

    public UserMedicines getMedRemById(Integer medicineId)throws UserMedicineException, UserexceptionMessage ;

    public MedicineResponse syncMedicineHistory(Integer medId , List<MedicineHistoryDTO> medicineHistoryDTOS) throws UserMedicineException;

    public MedicineResponse getMedicineHistory(Integer medId) throws UserMedicineException;
}
///