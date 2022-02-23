package com.example.user_service.service;

import com.example.user_service.exception.UserCaretakerException;
import com.example.user_service.model.UserCaretaker;

import java.util.List;

public interface CareTakerService {

    public UserCaretaker saveCareTaker(UserCaretaker userCaretaker);

    public UserCaretaker updateCaretakerStatus(String c_id) throws UserCaretakerException;

    public List<UserCaretaker> getPatientCaretakerMap();

    public List<UserCaretaker> getPatientsUnderMe(String user_id);

    public List<UserCaretaker> getPatientRequests(String user_id);

    public List<UserCaretaker> getMyCaretakers(String user_id);

    public List<UserCaretaker> getCaretakerRequests(String user_id);
}
