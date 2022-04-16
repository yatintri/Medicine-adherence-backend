package com.example.user_service.service;

import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.dto.Medicinepojo;
import com.example.user_service.repository.UserMedRemRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import org.modelmapper.ModelMapper;
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

    public static  final String ERROR= "Please enter valid id";
    @Autowired
    UserMedicineRepository userMedicineRepository;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    UserMedRemRepository userMedRemRepository;

    Logger logger = LoggerFactory.getLogger(UserMedicineServiceImpl.class);

    @Override
    public UserMedicines saveUserMedicine(String userId, Medicinepojo medicinepojo) throws UserMedicineException , UserexceptionMessage {

        UserEntity user = userRepository.getuserbyid(userId);
        if(user==null)
        {
            throw new UserexceptionMessage(ERROR);
        }
        UserMedicines userMedicines= mapToEntity(medicinepojo);
        userMedicines.setUserEntity(user);
        UserMedicines userMedicines1 = userMedicineRepository.save(userMedicines);
        if(userMedicines1.getMedicineName() == null)
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
            throw new UserMedicineException("Data not found");
        }
        userMedicineRepository.save(userMedicines.get());

        return true;

    }

    @Override
    @Async
    public CompletableFuture<List<UserMedicines>> getallUserMedicines(String userId)  throws UserMedicineException , UserexceptionMessage{


        UserEntity user = userRepository.getuserbyid(userId);
        if(user == null)
        {
            throw  new UserexceptionMessage(ERROR);
        }
        List<UserMedicines> list =  user.getUserMedicines();

        return CompletableFuture.completedFuture(list);

    }

    @Override
    public UserMedicines editMedicineDetails(Integer medicineId , Medicinepojo medicinepojo)throws UserMedicineException, UserexceptionMessage {

        Optional<UserMedicines> userMeds = userMedicineRepository.findById(medicineId);
        if(userMeds.isEmpty())
        {
            throw  new UserexceptionMessage(ERROR);
        }
        userMeds= Optional.ofNullable(mapToEntity(medicinepojo));
        UserMedicines userMeds1 = userMedicineRepository.save(userMeds.get());
        if(userMeds1.getMedicineName() == null)
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
    public UserMedicines getMedRemById(Integer medicineId) throws UserMedicineException{
        UserMedicines userMedicines2 = userMedicineRepository.getmedrembyid(medicineId);
        if(userMedicines2 == null)
        {
            throw new UserMedicineException(ERROR);
        }
        return userMedicines2;
    }

    private UserMedicines mapToEntity(Medicinepojo medicinepojo){
        return mapper.map(medicinepojo, UserMedicines.class);
    }



}
///