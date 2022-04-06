package com.example.user_service.service;

import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserMedicines;

import java.util.List;
import java.util.concurrent.CompletableFuture;
//
public interface UserMedicineService {

    public UserMedicines saveUserMedicine(String userId, UserMedicines userMedicines)throws UserMedicineException, UserexceptionMessage;

    public boolean updateMedicineStatus(Integer medicineId) throws UserMedicineException;

    public CompletableFuture<List<UserMedicines>> getallUserMedicines(String userId) throws UserMedicineException, UserexceptionMessage;

    public UserMedicines editMedicineDetails(Integer medicineId , UserMedicines userMedicines)throws UserMedicineException, UserexceptionMessage;

    boolean syncdata(String userId , List<UserMedicines> list);

    public UserMedicines getMedRemById(Integer medicineId)throws UserMedicineException, UserexceptionMessage ;

}
///