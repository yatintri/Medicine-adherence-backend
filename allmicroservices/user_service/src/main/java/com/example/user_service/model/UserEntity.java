package com.example.user_service.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
@NamedEntityGraph(name="userdetail_graph" ,
        attributeNodes = @NamedAttributeNode(value = "userDetails"))
public class UserEntity {

  @Id
  @Column(name = "user_id")
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
          name = "UUID",
          strategy = "org.hibernate.id.UUIDGenerator"
  )
  private String user_id;

  @Column(name = "user_name")
    private String user_name;

  @Column(name = "email")
    private String email;

  @Column(name = "last_login")
  private String last_login;

  @Column(name = "created_at")
  private String created_at;

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
//