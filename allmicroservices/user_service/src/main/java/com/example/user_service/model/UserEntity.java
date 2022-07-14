package com.example.user_service.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.List;
/**
 * This is entity class for User
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
@NamedEntityGraph(name="userDetail_graph" ,
        attributeNodes = @NamedAttributeNode(value = "userDetails"))
public class UserEntity {

  @Id
  @Column(name = "user_id",nullable = false,length = 100)
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
          name = "UUID",
          strategy = "org.hibernate.id.UUIDGenerator"
  )
  private String userId;

  @Column(name = "user_name",nullable = false,length = 100)
    private String userName;

  @Column(name = "email",nullable = false,length = 50)
  @Email
    private String email;

  @Column(name = "last_login",nullable = false,length = 30)
  private LocalDateTime lastLogin;

  @Column(name = "created_at",nullable = false,length = 30)
  private LocalDateTime createdAt;

  @OneToOne(
          cascade = CascadeType.ALL,
          mappedBy = "user",
          fetch = FetchType.LAZY
  )
  private UserDetails userDetails;


  @OneToMany(
          cascade = CascadeType.ALL,
          mappedBy = "userEntity",
          fetch = FetchType.EAGER
  )
  @JsonIgnore
  private List<UserMedicines> userMedicines;





}
///