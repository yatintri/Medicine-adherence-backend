package com.example.user_service.repository;

import com.example.user_service.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is image repository class
 */
@Repository
public interface ImageRepository extends JpaRepository<Image,String> {
}
