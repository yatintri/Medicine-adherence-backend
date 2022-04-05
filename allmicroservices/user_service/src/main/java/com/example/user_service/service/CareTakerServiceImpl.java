package com.example.user_service.service;

import com.example.user_service.exception.UserCaretakerException;
import com.example.user_service.model.UserCaretaker;
import com.example.user_service.pojos.caretakerpojos.UserCaretakerpojo;
import com.example.user_service.repository.UserCaretakerRepository;
import com.example.user_service.util.Datehelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CareTakerServiceImpl implements CareTakerService{

    @Autowired
    private UserCaretakerRepository userCaretakerRepository;

//    @Override
//    public List<UserCaretaker> getPatientCaretakerMap(){
//        return userCaretakerRepository.findAll();
//    }

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
        UserCaretaker uc = userCaretakerRepository.getById(cId);
        if(uc.getPatientName() == null){
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

//    @Override
//    public List<UserCaretaker> getPatientRequestStatus(String user_id) {
//        return userCaretakerRepository.getpatientrequeststatus(user_id);
//    }

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




}
