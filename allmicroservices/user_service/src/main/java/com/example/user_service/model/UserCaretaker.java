package com.example.user_service.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * This is entity class for Caretaker
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "my_caretakers")
public class UserCaretaker implements Serializable {
    @Id
    @Column(
            name = "id",
            nullable = false,
            length = 100
    )
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    @Column(
            name = "patient_name",
            nullable = false,
            length = 100
    )
    private String patientName;
    @Column(
            name = "request_status",
            nullable = false
    )
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean requestStatus;
    @Column(
            name = "caretaker_id",
            nullable = false,
            length = 100
    )
    private String caretakerId;
    @Column(
            name = "patient_id",
            nullable = false,
            length = 100
    )
    private String patientId;
    @Column(
            name = "caretaker_username",
            nullable = false,
            length = 100
    )
    private String caretakerUsername;
    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;
    @Column(
            name = "sent_by",
            nullable = false,
            length = 100
    )
    private String sentBy;
    @Column(
            name = "delete",
            nullable = false
    )
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean delete;

}

