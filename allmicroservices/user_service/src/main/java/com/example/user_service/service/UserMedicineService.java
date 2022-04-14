package com.example.user_service.service;

import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.Medicinepojo;

import java.util.List;
import java.util.concurrent.CompletableFuture;
//
public interface UserMedicineService {

    public UserMedicines saveUserMedicine(String userId, Medicinepojo medicinepojo)throws UserMedicineException, UserexceptionMessage;

    public boolean updateMedicineStatus(Integer medicineId) throws UserMedicineException;

    public CompletableFuture<List<UserMedicines>> getallUserMedicines(String userId) throws UserMedicineException, UserexceptionMessage;

    public UserMedicines editMedicineDetails(Integer medicineId , Medicinepojo medicinepojo)throws UserMedicineException, UserexceptionMessage;

    boolean syncdata(String userId , List<UserMedicines> list);

    public UserMedicines getMedRemById(Integer medicineId)throws UserMedicineException, UserexceptionMessage ;

}
///