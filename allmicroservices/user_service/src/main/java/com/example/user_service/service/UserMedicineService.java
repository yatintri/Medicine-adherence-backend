package com.example.user_service.service;

import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.dto.request.MedicineHistoryDTO;
import com.example.user_service.pojos.dto.request.MedicinePojo;
import com.example.user_service.pojos.dto.response.image.ImageListResponse;
import com.example.user_service.pojos.dto.response.medicine.MedicineResponse;
import com.example.user_service.pojos.dto.response.medicine.SyncResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This is an interface for user medicine service
 */
public interface UserMedicineService {


    CompletableFuture<List<UserMedicines>> getallUserMedicines(String userId,int page,int limit) ;

    SyncResponse syncData(String userId , List<MedicinePojo> list) ;

    MedicineResponse syncMedicineHistory(Integer medId , List<MedicineHistoryDTO> medicineHistoryDTOS) ;

    MedicineResponse getMedicineHistory(Integer medId, int page, int limit) ;

    ImageListResponse getUserMedicineImages(Integer medId, int page, int limit);

}
