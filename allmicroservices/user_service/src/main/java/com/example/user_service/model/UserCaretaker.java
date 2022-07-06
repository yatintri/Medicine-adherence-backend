package com.example.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "my_caretakers")
public class UserCaretaker {

    @Id
    @Column(name = "c_id",nullable = false,length = 100)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String cId;

    @Column(name = "patient_name",nullable = false,length = 100)
    private String patientName;

    @Column(name = "req_status",nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean reqStatus;

    @Column(name = "caretaker_id",nullable = false,length = 100)
    private String caretakerId;

    @Column(name = "patient_id",nullable = false,length = 100)
    private String patientId;

    @Column(name = "caretaker_username",nullable = false,length = 100)
    private String caretakerUsername;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "sent_by",nullable = false,length = 100)
    private String sentBy;
}
