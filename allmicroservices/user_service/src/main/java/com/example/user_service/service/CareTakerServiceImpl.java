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

    @Override
    public List<UserCaretaker> getPatientCaretakerMap(){
        return userCaretakerRepository.findAll();
    }

    @Override
    public UserCaretaker saveCareTaker(UserCaretakerpojo userCaretakerpojo) {

        UserCaretaker userCaretaker = new UserCaretaker();

        userCaretaker.setCaretaker_username(userCaretakerpojo.getCaretaker_username());
        userCaretaker.setReq_status(userCaretakerpojo.getReq_status());
        userCaretaker.setPatient_name(userCaretakerpojo.getPatient_name());
        userCaretaker.setCaretaker_id(userCaretakerpojo.getCaretaker_id());
        userCaretaker.setPatient_id(userCaretakerpojo.getPatient_id());
        userCaretaker.setCreated_at(Datehelper.getcurrentdatatime());
        userCaretaker.setSent_by(userCaretakerpojo.getSent_by());

        return userCaretakerRepository.save(userCaretaker);
    }

    @Override
    public UserCaretaker updateCaretakerStatus(String c_id) throws UserCaretakerException {
        UserCaretaker uc = userCaretakerRepository.getById(c_id);
        if(uc.getPatient_name() == null){
            throw new UserCaretakerException("No user found with this id");
        }
        uc.setReq_status(true);
        return userCaretakerRepository.save(uc);
    }

    @Override
    public List<UserCaretaker> getPatientsUnderMe(String user_id) {
        return userCaretakerRepository.getpatientsunderme(user_id);
    }

    @Override
    public List<UserCaretaker> getPatientRequests(String user_id) {
        return userCaretakerRepository.getpatientrequests(user_id);
    }

    @Override
    public List<UserCaretaker> getMyCaretakers(String user_id) {
        return userCaretakerRepository.getmycaretakers(user_id);
    }

    @Override
    public List<UserCaretaker> getCaretakerRequestStatus(String user_id) {
        return userCaretakerRepository.getcaretakerequeststatus(user_id);
    }

    @Override
    public List<UserCaretaker> getPatientRequestStatus(String user_id) {
        return userCaretakerRepository.getpatientrequeststatus(user_id);
    }

    @Override
    public List<UserCaretaker> getCaretakerRequestsP(String user_id){
        return userCaretakerRepository.getcaretakerrequestsp(user_id);
    }

    @Override
     public  Boolean delPatientReq(String c_id) {


        try{
            Optional<UserCaretaker> userCaretaker = userCaretakerRepository.findById(c_id);
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
