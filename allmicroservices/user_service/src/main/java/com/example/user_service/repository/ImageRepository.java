package com.example.user_service.repository;

import com.example.user_service.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This is image repository class
 */
public interface ImageRepository extends JpaRepository<Image,String> {
}
