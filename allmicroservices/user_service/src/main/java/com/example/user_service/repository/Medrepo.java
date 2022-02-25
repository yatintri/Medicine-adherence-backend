package com.example.user_service.repository;

import com.example.user_service.model.MedicineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Medrepo extends JpaRepository<MedicineEntity , Integer> {

    @Query("select meds from MedicineEntity meds where lower(meds.med_name) like lower(concat(?1,'%'))")
    List<MedicineEntity> getmedicines(String search_med);


}
//