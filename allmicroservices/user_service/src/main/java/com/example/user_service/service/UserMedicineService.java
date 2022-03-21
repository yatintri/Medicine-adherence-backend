package com.example.user_service.service;

import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserMedReminder;
import com.example.user_service.model.UserMedicines;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserMedicineService {

    public UserMedicines saveUserMedicine(String user_id, UserMedicines userMedicines)throws UserMedicineException, UserexceptionMessage;

    public boolean updateMedicineStatus(String medicine_id) throws UserMedicineException;

    public CompletableFuture<List<UserMedicines>> getallUserMedicines(String user_id) throws UserMedicineException, UserexceptionMessage;

    public UserMedicines editMedicineDetails(String medicine_id , UserMedicines userMedicines)throws UserMedicineException, UserexceptionMessage;


    public UserMedicines getMedReminders();

    public UserMedicines getMedRemById(String medicine_id);

    public UserMedicines saveMedReminder(UserMedReminder userMedReminder);
}
//