package com.example.user_service.repository;

import com.example.user_service.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * This is a User details repository class
 */
public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {
}
