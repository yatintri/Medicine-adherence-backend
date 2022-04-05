package com.example.user_service.service;

import com.example.user_service.exception.UserCaretakerException;
import com.example.user_service.model.UserCaretaker;
import com.example.user_service.pojos.caretakerpojos.UserCaretakerpojo;

import java.util.List;

public interface CareTakerService {

    public UserCaretaker saveCareTaker(UserCaretakerpojo userCaretakerpojo);

    public UserCaretaker updateCaretakerStatus(String c_id) throws UserCaretakerException;

    public List<UserCaretaker> getPatientCaretakerMap();

    public List<UserCaretaker> getPatientsUnderMe(String user_id);

    public List<UserCaretaker> getPatientRequests(String user_id);

    public List<UserCaretaker> getMyCaretakers(String user_id);

    public List<UserCaretaker> getCaretakerRequestStatus(String user_id);

    public List<UserCaretaker> getPatientRequestStatus(String user_id);

    public List<UserCaretaker> getCaretakerRequestsP(String user_id);

    public Boolean delPatientReq(String c_id);
}
