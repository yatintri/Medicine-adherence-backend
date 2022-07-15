package com.example.user_service.repository;

import com.example.user_service.model.MedicineHistory;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This is a medicine history repository class
 */
@Repository
public interface UserMedHistoryRepository extends PagingAndSortingRepository<MedicineHistory,Integer> {


}
