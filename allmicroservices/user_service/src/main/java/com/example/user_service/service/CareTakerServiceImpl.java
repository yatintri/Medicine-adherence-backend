package com.example.user_service.service;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.example.user_service.exception.UserCaretakerException;
import com.example.user_service.model.UserCaretaker;
import com.example.user_service.pojos.Notificationmessage;
import com.example.user_service.pojos.caretakerpojos.UserCaretakerpojo;
import com.example.user_service.repository.UserCaretakerRepository;
import com.example.user_service.util.Datehelper;
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
    RabbitTemplate rabbitTemplate;

    @Override
    public UserCaretaker saveCareTaker(UserCaretakerpojo userCaretakerpojo) {

        UserCaretaker userCaretaker = new UserCaretaker();

        userCaretaker.setCaretakerUsername(userCaretakerpojo.getCaretakerUsername());
        userCaretaker.setReqStatus(userCaretakerpojo.getReqStatus());
        userCaretaker.setPatientName(userCaretakerpojo.getPatientName());
        userCaretaker.setCaretakerId(userCaretakerpojo.getCaretakerId());
        userCaretaker.setPatientId(userCaretakerpojo.getPatientId());
        userCaretaker.setCreatedAt(Datehelper.getcurrentdatatime());
        userCaretaker.setSentBy(userCaretakerpojo.getSentBy());

        return userCaretakerRepository.save(userCaretaker);
    }

    @Override
    public UserCaretaker updateCaretakerStatus(String cId) throws UserCaretakerException {
        System.out.println(cId);

        UserCaretaker uc = userCaretakerRepository.findById(cId).get();

        System.out.println(uc);
        if(uc == null){
            throw new UserCaretakerException("No user found with this id");
        }
        uc.setReqStatus(true);
        return userCaretakerRepository.save(uc);
    }

    @Override
    public List<UserCaretaker> getPatientsUnderMe(String userId) {
        return userCaretakerRepository.getpatientsunderme(userId);
    }

    @Override
    public List<UserCaretaker> getPatientRequests(String userId) {
        return userCaretakerRepository.getpatientrequests(userId);
    }

    @Override
    public List<UserCaretaker> getMyCaretakers(String userId) {
        return userCaretakerRepository.getmycaretakers(userId);
    }

    @Override
    public List<UserCaretaker> getCaretakerRequestStatus(String userId) {
        return userCaretakerRepository.getcaretakerequeststatus(userId);
    }



    @Override
    public List<UserCaretaker> getCaretakerRequestsP(String userId){
        return userCaretakerRepository.getcaretakerrequestsp(userId);
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
    public boolean sendimagetocaretaker(MultipartFile multipartFile, String filename, String caretakerid) throws IOException , UserCaretakerException {

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


//

}