package com.example.user_service.service;

import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedReminder;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.repository.UserMedRemRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UserMedicineServiceImpl implements UserMedicineService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMedicineRepository userMedicineRepository;

    @Autowired
    UserMedRemRepository userMedRemRepository;

    Logger logger = LoggerFactory.getLogger(UserMedicineServiceImpl.class);

    @Override
    public UserMedicines saveUserMedicine( String userId, UserMedicines userMedicines) throws UserMedicineException , UserexceptionMessage {

        UserEntity user = userRepository.getuserbyid(userId);
        if(user==null)
        {
            throw new UserexceptionMessage("Please enter valid id");
        }
        userMedicines.setUserEntity(user);
     //   userMedicines.setCreate_time(Datehelper.getcurrentdate().toString());
        UserMedicines userMedicines1 = userMedicineRepository.save(userMedicines);
        if(userMedicines1 == null)
        {
            throw new UserMedicineException("Error try again!");
        }
        return userMedicines1;
    }

    @Override
    public boolean updateMedicineStatus(Integer medicineId)  throws UserMedicineException{


        Optional<UserMedicines> userMedicines = userMedicineRepository
                                     .findById(medicineId);
        if(userMedicines.isEmpty())
        {
            throw new UserMedicineException("Medicine not found");
        }
      //  userMedicines.get().setActive_status(false);
        userMedicineRepository.save(userMedicines.get());

        return true;

    }

    @Override
    @Async
    public CompletableFuture<List<UserMedicines>> getallUserMedicines(String userId)  throws UserMedicineException , UserexceptionMessage{


        UserEntity user = userRepository.getuserbyid(userId);
        if(user == null)
        {
            throw  new UserexceptionMessage("Please enter valid id");
        }
        List<UserMedicines> list =  user.getUserMedicines();

        return CompletableFuture.completedFuture(list);

    }

    @Override
    public UserMedicines editMedicineDetails(Integer medicineId , UserMedicines userMedicines)throws UserMedicineException, UserexceptionMessage {

        Optional<UserMedicines> userMeds = userMedicineRepository.findById(medicineId);
        if(userMeds.isEmpty())
        {
            throw  new UserexceptionMessage("Please enter valid id");
        }
        userMeds.get().setMedicineDes(userMedicines.getMedicineDes());
        userMeds.get().setMedicineName(userMedicines.getMedicineName());
        UserMedicines userMeds1 = userMedicineRepository.save(userMeds.get());
        if(userMeds1 == null)
        {
            throw new UserMedicineException("Error try again!");
        }
        return userMeds1;
    }

    @Override
    public boolean syncdata(String userId, List<UserMedicines> list) {

        UserEntity user = userRepository.getuserbyid(userId);
        for(UserMedicines userMedicines : list){

            userMedicines.setUserEntity(user);

        }




        return false;
    }

    @Override
    public UserMedicines getMedRemById(Integer medicineId) throws UserMedicineException, UserexceptionMessage {
        UserMedicines userMedicines2 = userMedicineRepository.getmedrembyid(medicineId);
        if(userMedicines2 == null)
        {
            throw new UserMedicineException("Please Enter Valid Id!!!");
        }
        return userMedicines2;
    }

    @Override
    public UserMedReminder saveMedReminder(UserMedReminder userMedReminder,Integer medicineId) throws UserMedicineException, UserexceptionMessage {
        UserMedicines userMedicines =  userMedicineRepository.findById(medicineId).get();
        userMedReminder.setUserRem(userMedicines);
        UserMedReminder userMedReminder1 = userMedRemRepository.save(userMedReminder);
        if(userMedReminder1 == null){
            throw new UserMedicineException("Error try again");
        }
        return userMedReminder1;
    }




}
//