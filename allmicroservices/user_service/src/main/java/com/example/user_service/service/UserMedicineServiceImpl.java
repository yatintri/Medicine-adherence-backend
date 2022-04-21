package com.example.user_service.service;

import com.example.user_service.exception.UserMedicineException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.MedicineHistory;
import com.example.user_service.model.UserEntity;
import com.example.user_service.model.UserMedicines;
import com.example.user_service.pojos.dto.MedicineHistoryDTO;
import com.example.user_service.pojos.dto.Medicinepojo;
import com.example.user_service.pojos.response.MedicineResponse;
import com.example.user_service.repository.UserMedHistoryRepository;
import com.example.user_service.repository.UserMedicineRepository;
import com.example.user_service.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class UserMedicineServiceImpl implements UserMedicineService {

    @Autowired
    UserRepository userRepository;

    public static final String ERROR = "Please enter valid id";
    @Autowired
    UserMedicineRepository userMedicineRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    UserMedHistoryRepository userMedHistoryRepository;

    Logger logger = LoggerFactory.getLogger(UserMedicineServiceImpl.class);

    @Override
    @Async
    public CompletableFuture<List<UserMedicines>> getallUserMedicines(String userId) throws UserMedicineException, UserexceptionMessage {


        UserEntity user = userRepository.getUserById(userId);
        if (user == null) {
            throw new UserexceptionMessage(ERROR);
        }
        List<UserMedicines> list = user.getUserMedicines();

        return CompletableFuture.completedFuture(list);

    }


    @Override
    public boolean syncData(String userId, List<UserMedicines> list) throws UserMedicineException {

        UserEntity user = userRepository.getUserById(userId);
        if(user.getUserMedicines().isEmpty()){
            throw new UserMedicineException("Unable to sync");
        }
        for (UserMedicines userMedicines : list) {

            userMedicines.setUserEntity(user);

        }

        return false;
    }


    @Override
    @Async
    public MedicineResponse syncMedicineHistory(Integer medId, List<MedicineHistoryDTO> medicineHistoryDTOS) throws UserMedicineException {

        UserMedicines userMedicines = userMedicineRepository.getMedById(medId);
        if(userMedicines == null){
            throw new UserMedicineException("Unable to sync");

        }

        List<MedicineHistory> medicineHistories = medicineHistoryDTOS.stream().map(medHid -> {
            MedicineHistory medicineHistory1 = new MedicineHistory();
            medicineHistory1.setHistoryId(medHid.getRemId());
            medicineHistory1.setDate(medHid.getDate());
            medicineHistory1.setTaken(String.join(",", medHid.getTaken()));
            medicineHistory1.setNottaken(String.join(",", medHid.getNot_taken()));
            medicineHistory1.setUserMedicines(userMedicines);
            return medicineHistory1;
        }).collect(Collectors.toList());
        CompletableFuture.completedFuture(userMedHistoryRepository.saveAll(medicineHistories));

        return null;
    }

    @Override
    public MedicineResponse getMedicineHistory(Integer medId) throws UserMedicineException {

      List<MedicineHistory> medicineHistories =  userMedicineRepository.getMedById(medId).getMedicineHistories();
      if(medicineHistories.isEmpty()){
          throw new UserMedicineException("No record found!!");
      }
        return new MedicineResponse("OK" , "Medicine History" , medicineHistories);
    }




}
///