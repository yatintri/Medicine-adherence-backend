package com.example.user_service.service;

import com.example.user_service.model.UserMedicines;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserMedicineService {

    public UserMedicines saveUserMedicine(String user_id, UserMedicines userMedicines);

    public boolean updateMedicineStatus(String medicine_id);

    public CompletableFuture<List<UserMedicines>> getallUserMedicines(String user_id);

    public UserMedicines editMedicineDetails(String medicine_id , UserMedicines userMedicines);

}
