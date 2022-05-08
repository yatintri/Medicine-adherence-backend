package com.example.user_service.repository;

import com.example.user_service.model.UserCaretaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCaretakerRepository extends JpaRepository<UserCaretaker, String> {


    @Query("SELECT u from UserCaretaker u where u.reqStatus=true and u.caretakerId = ?1")
    List<UserCaretaker> getPatientsUnderMe(String id);

    @Query("select u from UserCaretaker u where u.reqStatus=false and u.caretakerId = ?1")
    List<UserCaretaker> getPatientRequests(String id);

    @Query("SELECT u from UserCaretaker u where u.reqStatus=true and u.patientId = ?1")
    List<UserCaretaker> getMyCaretakers(String id);

    @Query("select u from UserCaretaker u where u.reqStatus=false and u.patientId = ?1")
    List<UserCaretaker> getCaretakerRequestStatus(String id);

    @Query("select u from UserCaretaker u where u.reqStatus=false and u.caretakerId = ?1")
    List<UserCaretaker> getPatientRequestStatus(String id);

    @Query("select u from UserCaretaker u where u.sentBy='c' and u.patientId= ?1 and u.reqStatus=false")
    List<UserCaretaker> getCaretakerRequestsP(String userId);


}
///