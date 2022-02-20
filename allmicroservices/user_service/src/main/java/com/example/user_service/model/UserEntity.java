package com.example.user_service.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int user_id;

  @Column(name = "user_name")
    private String user_name;

  @Column(name = "email")
    private String email;

  @OneToOne(
          cascade = CascadeType.ALL,
          mappedBy = "user",
          fetch = FetchType.LAZY
  )
  private UserDetails userDetails;




}
