package com.example.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

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

    @Column(name = "patient_name")
    private String patient_name;

    @Column(name = "req_status")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean req_status;

    @Column(name = "caretaker_id")
    private String caretaker_id;

    @Column(name = "patient_id")
    private String patient_id;

    @Column(name = "caretaker_username")
    private String caretaker_username;

    @Column(name = "created_at")
    private String created_at;

    @Column(name = "sent_by")
    private String sent_by;
}
//