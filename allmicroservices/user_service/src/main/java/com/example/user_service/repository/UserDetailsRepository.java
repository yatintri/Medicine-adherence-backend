package com.example.user_service.repository;

import com.example.user_service.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is a User details repository class
 */
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {
}
