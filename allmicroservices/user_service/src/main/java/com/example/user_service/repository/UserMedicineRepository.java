package com.example.user_service.repository;

import com.example.user_service.model.UserMedicines;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserMedicineRepository extends JpaRepository<UserMedicines,String> {


    @Query("SELECT u from UserMedicines u where u.medicine_id = ?1")
    public UserMedicines getMedRemById(String medicine_id);
}
