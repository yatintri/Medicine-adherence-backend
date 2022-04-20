package com.example.user_service.service;


import com.example.user_service.exception.UserCaretakerException;
import com.example.user_service.exception.UserexceptionMessage;
import com.example.user_service.model.UserCaretaker;
import com.example.user_service.pojos.Notificationmessage;
import com.example.user_service.pojos.dto.UserCaretakerDTO;
import com.example.user_service.repository.UserCaretakerRepository;
import com.example.user_service.util.Datehelper;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class CareTakerServiceImpl implements CareTakerService{

    @Autowired
    private UserCaretakerRepository userCaretakerRepository;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public UserCaretaker saveCareTaker(UserCaretakerDTO userCaretakerDTO) {

        UserCaretaker userCaretaker = mapToEntity(userCaretakerDTO);
        userCaretaker.setCreatedAt(Datehelper.getcurrentdatatime());

        return userCaretakerRepository.save(userCaretaker);
    }

    @Override
    public UserCaretaker updateCaretakerStatus(String cId) throws UserCaretakerException {
        Optional<UserCaretaker> uc = userCaretakerRepository.findById(cId);
        if(uc.isEmpty()){
            throw new UserCaretakerException("User not found");
        }
        uc.get().setReqStatus(true);
        return userCaretakerRepository.save(uc.get());
    }

    @Override
    public List<UserCaretaker> getPatientsUnderMe(String userId) throws UserCaretakerException{

        List<UserCaretaker> userCaretaker = userCaretakerRepository.getPatientsUnderMe(userId);
        if(userCaretaker.isEmpty()){
            throw new UserCaretakerException("Data not found");
        }
        return userCaretaker;
    }

    @Override
    public List<UserCaretaker> getPatientRequests(String userId) throws UserCaretakerException {
        List<UserCaretaker> userCaretaker = userCaretakerRepository.getPatientRequests(userId);
        if(userCaretaker.isEmpty()){
            throw new UserCaretakerException("Data not found");
        }
        return userCaretaker;
    }

    @Override
    public List<UserCaretaker> getMyCaretakers(String userId) throws UserCaretakerException {
        List<UserCaretaker> userCaretaker = userCaretakerRepository.getMyCaretakers(userId);
        if(userCaretaker.isEmpty()){
            throw new UserCaretakerException("Data not found");
        }
        return userCaretaker;
    }

    @Override
    public List<UserCaretaker> getCaretakerRequestStatus(String userId) {
        return userCaretakerRepository.getCaretakerRequestStatus(userId);
    }



    @Override
    public List<UserCaretaker> getCaretakerRequestsP(String userId) throws UserCaretakerException {
        List<UserCaretaker> userCaretaker = userCaretakerRepository.getCaretakerRequestsP(userId);
        if(userCaretaker.isEmpty()){
            throw new UserCaretakerException("Data not found");
        }
        return userCaretaker;
    }

    @Override
    public  Boolean delPatientReq(String cId) {


        try{
            Optional<UserCaretaker> userCaretaker = userCaretakerRepository.findById(cId);
            if (userCaretaker.isPresent()){
                userCaretakerRepository.delete(userCaretaker.get());
                return true;

            }
            return false;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public boolean sendImageToCaretaker(MultipartFile multipartFile, String filename, String caretakerid) throws IOException , UserCaretakerException {

        try{
            File file = new File(System.getProperty("user.dir")+"/src/main/upload/static/images");
            if(!file.exists()){
                file.mkdir();
            }
            Path path = Paths.get(System.getProperty("user.dir")+"/src/main/upload/static/images",filename.concat(".").concat("jpg"));
            Files.write(path,multipartFile.getBytes());

            String fcmToken = "c_nl_oj2S9S_HmPQjfvDSR:APA91bEYDLIGXU4jI4P26uVqAdoVaaJ378TtGjxrKaytbuqulXWZGs91Jx6_1mrLWEaGECufvZ512BWwQvCAQTnjg3OTh2GPn5E3DNOTh_ycy4Xi7-LZ39OFsGXYjiUm5UDJfRez0CV4";
            rabbitTemplate.convertAndSend("project_exchange","notification_key",new Notificationmessage(fcmToken,"Take medicine","caretaker","",filename+".jpg"));

        }catch (Exception e){
            return false;
        }

        return true;
    }

    private UserCaretaker mapToEntity(UserCaretakerDTO userCaretakerDTO){
        return mapper.map(userCaretakerDTO, UserCaretaker.class);

    }

//

}