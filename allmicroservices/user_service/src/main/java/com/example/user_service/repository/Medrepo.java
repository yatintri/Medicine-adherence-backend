package com.example.user_service.repository;

import com.example.user_service.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @Deprecated
 */
@Repository
public interface Medrepo extends JpaRepository<Medicine, Integer> {

    @Query("select meds from Medicine meds where lower(meds.medName) like lower(concat(?1,'%'))")
    List<Medicine> getMedicines(String searchMed);


}
