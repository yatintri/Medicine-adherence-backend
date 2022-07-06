package com.example.user_service.repository;

import com.example.user_service.model.MedicineHistory;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserMedHistoryRepository extends PagingAndSortingRepository<MedicineHistory,Integer> {


}
