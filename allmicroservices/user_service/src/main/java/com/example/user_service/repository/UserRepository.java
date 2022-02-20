package com.example.user_service.repository;

import com.example.user_service.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Query("select u from UserEntity u where lower(u.user_name) like lower(concat('%', ?1,'%'))")
    public List<UserEntity> findByNameIgnoreCase(String user_name);

    public UserEntity findByEmailIgnoreCase(String email);

}