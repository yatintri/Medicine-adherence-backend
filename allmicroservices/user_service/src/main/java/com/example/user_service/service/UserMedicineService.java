package com.example.user_service.service;

import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserMedReminder;
import com.example.user_service.model.UserMedicines;

import java.util.List;
import java.util.concurrent.CompletableFuture;
//
public interface UserMedicineService {

    public UserMedicines saveUserMedicine(String user_id, UserMedicines userMedicines)throws UserMedicineException, UserexceptionMessage;

    public boolean updateMedicineStatus(Integer medicine_id) throws UserMedicineException;

    public CompletableFuture<List<UserMedicines>> getallUserMedicines(String user_id) throws UserMedicineException, UserexceptionMessage;

    public UserMedicines editMedicineDetails(Integer medicine_id , UserMedicines userMedicines)throws UserMedicineException, UserexceptionMessage;

    boolean syncdata(String user_id , List<UserMedicines> list);

    public UserMedicines getMedRemById(Integer medicine_id)throws UserMedicineException, UserexceptionMessage ;

    public UserMedReminder saveMedReminder(UserMedReminder userMedReminder, Integer medicine_id)throws UserMedicineException, UserexceptionMessage ;
}
///