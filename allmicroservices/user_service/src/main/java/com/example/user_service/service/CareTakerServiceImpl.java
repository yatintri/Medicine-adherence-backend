package com.example.user_service.service;

import com.example.user_service.model.UserCaretaker;
import com.example.user_service.repository.UserCaretakerRepository;
import com.example.user_service.util.Datehelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareTakerServiceImpl implements CareTakerService{

    @Autowired
    private UserCaretakerRepository userCaretakerRepository;

    @Override
    public List<UserCaretaker> getPatientCaretakerMap(){
        return userCaretakerRepository.findAll();
    }

    @Override
    public UserCaretaker saveCareTaker(UserCaretaker userCaretaker) {
        userCaretaker.setCreated_at(Datehelper.getcurrentdatatime());
        return userCaretakerRepository.save(userCaretaker);
    }

    @Override
    public UserCaretaker updateCaretakerStatus(String c_id) {
        UserCaretaker uc= userCaretakerRepository.getById(c_id);
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
    public List<UserCaretaker> getCaretakerRequests(String user_id) {
        return userCaretakerRepository.getcaretakerequests(user_id);
    }
}
