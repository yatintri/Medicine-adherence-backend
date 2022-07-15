package com.example.user_service.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.user_service.model.UserEntity;

/**
 * This is a user repository class
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @Query("SELECT user from UserEntity user")
    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            value = "userDetail_graph"
    )
    Page<UserEntity> findAllUsers(Pageable pageable);

    @Query("select u from UserEntity u where lower(u.email) like lower(?1)")
    public UserEntity findByMail(String email);

    @Query("select u from UserEntity u where lower(u.userName) like lower(concat(?1,'%'))")
    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            value = "userDetail_graph"
    )
    public List<UserEntity> findByNameIgnoreCase(String userName);

    @Query("SELECT u from UserEntity u where u.userId = ?1")
    public UserEntity getUserById(String userId);
}

