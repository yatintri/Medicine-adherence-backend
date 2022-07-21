package com.example.user_service.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This is entity class for User
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
@NamedEntityGraph(name = "userDetail_graph",
        attributeNodes = @NamedAttributeNode(value = "userDetails"))
public class User implements Serializable {

    @Id
    @Column(name = "user_id", nullable = false, length = 100)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String userId;

    @Column(name = "user_name", nullable = false, length = 100)
    private String userName;

    @Column(name = "email", nullable = false, length = 50)
    @Email
    private String email;

    @Column(name = "last_login", nullable = false, length = 30)
    private LocalDateTime lastLogin;

    @Column(name = "created_at", nullable = false, length = 30)
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    @OneToOne(
            cascade = CascadeType.ALL,
            mappedBy = "user"
    )

    private UserDetails userDetails;


    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "userEntity",
            fetch = FetchType.EAGER
    )
    @JsonIgnore
    private transient List<UserMedicines> userMedicines;


}
