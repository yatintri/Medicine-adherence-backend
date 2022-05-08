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
    private String cId;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "req_status")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean reqStatus;

    @Column(name = "caretaker_id")
    private String caretakerId;

    @Column(name = "patient_id")
    private String patientId;

    @Column(name = "caretaker_username")
    private String caretakerUsername;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "sent_by")
    private String sentBy;
}
///