package com.example.user_service.repository;

import java.util.List;

import com.example.user_service.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * This is a user repository class
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT user from User user")
    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            value = "userDetail_graph"
    )
    Page<User> findAllUsers(Pageable pageable);

    @Query("select u from User u where lower(u.email) like lower(?1)")
     User findByMail(String email);

    @Query("select u from User u where lower(u.userName) like lower(concat(?1,'%'))")
    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            value = "userDetail_graph"
    )
     List<User> findByNameIgnoreCase(String userName);

    @Query("SELECT u from User u where u.userId = ?1")
     User getUserById(String userId);
}

