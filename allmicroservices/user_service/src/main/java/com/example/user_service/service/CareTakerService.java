package com.example.user_service.service;

import com.example.user_service.model.UserCaretaker;

import java.util.List;

public interface CareTakerService {
    public UserCaretaker saveCareTaker(UserCaretaker userCaretaker);

    public UserCaretaker updatecaretakerStatus(String c_id);

    public List<UserCaretaker> getPatientsUnderMe(String user_id);
}
