package com.example.user_service.repository;

import com.example.user_service.model.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Query("select u from UserEntity u where lower(u.user_name) like lower(concat(?1,'%'))")
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,value = "userdetail_graph")
    public List<UserEntity> findByNameIgnoreCase(String user_name);

    @Query("select u from UserEntity u where lower(u.email) like lower(?1)")
    public UserEntity findBymail(String email);

    @Query("SELECT u from UserEntity u where u.user_id = ?1")
    public UserEntity getuserbyid(String user_id);

    @Query("SELECT user from UserEntity user")
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,value = "userdetail_graph")
    List<UserEntity> findAllusers();

}//