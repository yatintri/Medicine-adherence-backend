package com.example.user_service.repository;

import com.example.user_service.model.UserCaretaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCaretakerRepository extends JpaRepository<UserCaretaker, String> {


    @Query("SELECT u from UserCaretaker u where u.reqStatus=true and u.caretakerId = ?1")
    List<UserCaretaker> getpatientsunderme(String id);

    @Query("select u from UserCaretaker u where u.reqStatus=false and u.caretakerId = ?1")
    List<UserCaretaker> getpatientrequests(String id);

    @Query("SELECT u from UserCaretaker u where u.reqStatus=true and u.patientId = ?1")
    List<UserCaretaker> getmycaretakers(String id);

    @Query("select u from UserCaretaker u where u.reqStatus=false and u.patientId = ?1")
    List<UserCaretaker> getcaretakerequeststatus(String id);

    @Query("select u from UserCaretaker u where u.reqStatus=false and u.caretakerId = ?1")
    List<UserCaretaker> getpatientrequeststatus(String id);

    @Query("select u from UserCaretaker u where u.sentBy='c' and u.patientId= ?1 and u.reqStatus=false")
    List<UserCaretaker> getcaretakerrequestsp(String userId);


}
//