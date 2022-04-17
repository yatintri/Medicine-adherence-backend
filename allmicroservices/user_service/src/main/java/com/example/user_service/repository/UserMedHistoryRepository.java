package com.example.user_service.repository;

import com.example.user_service.model.MedicineHistory;
import com.example.user_service.model.UserMedReminder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMedHistoryRepository extends JpaRepository<MedicineHistory,Integer> {


}
//