package com.example.user_service.repository;

import com.example.user_service.model.UserMedicines;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * This is a medicine repository class
 */
@Repository
public interface UserMedicineRepository extends PagingAndSortingRepository<UserMedicines,Integer> {


    @Query("SELECT u from UserMedicines u where u.medicineId = ?1")
     UserMedicines getMedicineById(Integer medicineId);

    @Query("SELECT U FROM UserMedicines U WHERE U.days like (%?1%)")
    List<UserMedicines> getMedicinesForToday(String day , Pageable pageable);



}
