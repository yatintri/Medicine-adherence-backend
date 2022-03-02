package com.example.user_service.repository;

import com.example.user_service.model.UserMedicines;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMedicineRepository extends JpaRepository<UserMedicines,String> {
}
