package com.example.user_service.repository;

import com.example.user_service.model.UserCaretaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCaretakerRepository extends JpaRepository<UserCaretaker, String> {


    @Query("SELECT u from UserCaretaker u where u.req_status=true and u.caretaker_id = ?1")
    List<UserCaretaker> getpatientsunderme(String id);

    @Query("select u from UserCaretaker u where u.req_status=false and u.caretaker_id = ?1")
    List<UserCaretaker> getpatientrequests(String id);

    @Query("SELECT u from UserCaretaker u where u.req_status=true and u.patient_id = ?1")
    List<UserCaretaker> getmycaretakers(String id);

    @Query("select u from UserCaretaker u where u.req_status=false and u.patient_id = ?1")
    List<UserCaretaker> getcaretakerequeststatus(String id);

    @Query("select u from UserCaretaker u where u.req_status=false and u.caretaker_id = ?1")
    List<UserCaretaker> getpatientrequeststatus(String id);

    @Query("select u from UserCaretaker u where u.sent_by='c' and u.patient_id= ?1 and u.req_status=false")
    List<UserCaretaker> getcaretakerrequestsp(String userId);


}
//