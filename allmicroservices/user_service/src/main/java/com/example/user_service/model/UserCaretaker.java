package com.example.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "my_caretakers")
public class UserCaretaker {

    @Id
    @Column(name = "c_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String c_id;

    @Column(name = "pateint_name")
    private String pateint_username;

    @Column(name = "status")
    private boolean status;

    @Column(name = "caretaker_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String caretaker_id;

    @Column(name = "pateint_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String pateint_id;

    @Column(name = "caretaker_username")
    private String caretaker_username;

    @Column(name = "created_at")
    private java.sql.Date created_at;
}
