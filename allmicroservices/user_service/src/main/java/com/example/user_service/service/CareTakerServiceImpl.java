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
    public UserCaretaker saveCareTaker(UserCaretaker userCaretaker) {
        userCaretaker.setCreated_at(Datehelper.getcurrentdatatime());
        return userCaretakerRepository.save(userCaretaker);
    }

    @Override
    public UserCaretaker updatecaretakerStatus(String c_id) {
        return null;
    }

    @Override
    public List<UserCaretaker> getPatientsUnderMe(String user_id) {
        return userCaretakerRepository.getpatientsunderme(user_id);
    }
}
