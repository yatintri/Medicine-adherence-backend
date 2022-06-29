package com.example.user_service.repository;

import com.example.user_service.model.UserCaretaker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserCaretakerRepository extends PagingAndSortingRepository<UserCaretaker, String> {


    @Query("SELECT u from UserCaretaker u where u.reqStatus=true and u.caretakerId = ?1")
    Page<UserCaretaker> getPatientsUnderMe(String id, Pageable pageable);

    @Query("select u from UserCaretaker u where u.reqStatus=false and u.caretakerId = ?1")
    Page<UserCaretaker> getPatientRequests(String id,Pageable pageable);

    @Query("SELECT u from UserCaretaker u where u.reqStatus=true and u.patientId = ?1")
    Page<UserCaretaker> getMyCaretakers(String id,Pageable pageable);

    @Query("select u from UserCaretaker u where u.reqStatus=false and u.patientId = ?1")
    List<UserCaretaker> getCaretakerRequestStatus(String id);

    @Query("select u from UserCaretaker u where u.reqStatus=false and u.caretakerId = ?1")
    List<UserCaretaker> getPatientRequestStatus(String id);

    @Query("select u from UserCaretaker u where u.sentBy='c' and u.patientId= ?1 and u.reqStatus=false")
    Page<UserCaretaker> getCaretakerRequestsP(String userId,Pageable pageable);

    @Query("select u from UserCaretaker u where u.patientId=?1 and u.caretakerId=?2")
    UserCaretaker check(String patientId,String caretakerId);


}
///