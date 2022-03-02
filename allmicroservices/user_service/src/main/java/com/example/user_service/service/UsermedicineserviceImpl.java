package com.example.user_service.service;

import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UsermedicineserviceImpl implements UserMedicineService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMedicineRepository userMedicineRepository;

    Logger logger = LoggerFactory.getLogger(UsermedicineserviceImpl.class);

    @Override
    public UserMedicines saveUserMedicine(UserMedicines userMedicines , String user_id) {

        UserEntity user = userRepository.getByid(user_id);
        userMedicines.setUserEntity(user);
        return userMedicineRepository.save(userMedicines);


    }

    @Override
    public boolean updateMedicineStatus(String medicine_id) {


        UserMedicines userMedicines = userMedicineRepository
                                     .findById(medicine_id)
                                     .get();
        userMedicines.setActive_status(false);
        userMedicineRepository.save(userMedicines);

        return true;

    }

    @Override
    @Async
    public CompletableFuture<List<UserMedicines>> getallUserMedicines(String user_id) {


        UserEntity user = userRepository.getByid(user_id);
        List<UserMedicines> list =  user.getUserMedicines();
        return CompletableFuture.completedFuture(list);

    }

    @Override
    public UserMedicines editMedicineDetails(String medicine_id , UserMedicines userMedicines) {

        UserMedicines userMeds = userMedicineRepository.findById(medicine_id).get();
        userMeds.setMedicine_des(userMedicines.getMedicine_des());
        userMeds.setMedicine_name(userMedicines.getMedicine_name());
        return userMedicineRepository.save(userMeds);


    }


}
