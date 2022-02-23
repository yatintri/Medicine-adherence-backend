package com.example.user_service.repository;

import com.example.user_service.model.UserCaretaker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCaretakerRepository extends JpaRepository<UserCaretaker, String> {


    @Query("SELECT u from UserCaretaker u where u.caretaker_id = ?1")
    List<UserCaretaker> getpatientsunderme(String id);

}
