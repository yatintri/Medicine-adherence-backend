package com.example.user_service.repository;

import com.example.user_service.model.MedicineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
/**
 * @Deprecated
 */
public interface Medrepo extends JpaRepository<MedicineEntity , Integer> {

    @Query("select meds from MedicineEntity meds where lower(meds.medName) like lower(concat(?1,'%'))")
    List<MedicineEntity> getmedicines(String searchMed);


}
