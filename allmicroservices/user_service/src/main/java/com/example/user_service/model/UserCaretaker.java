package com.example.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    @NotNull(message = "Id is mandatory")
    @NotBlank(message = "Id is mandatory")
    private String cId;

    @Column(name = "patient_name")
    @NotNull(message = "Patient Name is mandatory")
    @NotBlank(message = "Patient Name is mandatory")
    private String patientName;

    @Column(name = "req_status")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean reqStatus;

    @Column(name = "caretaker_id")
    @NotNull(message = "Caretaker id is mandatory")
    @NotBlank(message = "Caretaker id is mandatory")
    private String caretakerId;

    @Column(name = "patient_id")
    @NotNull(message = "Patient Id is mandatory")
    @NotBlank(message = "Patient Id is mandatory")
    private String patientId;

    @Column(name = "caretaker_username")
    @NotNull(message = "Caretaker name is mandatory")
    @NotBlank(message = "Caretaker name is mandatory")
    private String caretakerUsername;

    @Column(name = "created_at")
    @NotNull(message = "Created at is mandatory")
    @NotBlank(message = "Created at is mandatory")
    private LocalDateTime createdAt;

    @Column(name = "sent_by")
    @NotNull(message = "Sent By is mandatory")
    @NotBlank(message = "Sent By is mandatory")
    private String sentBy;
}
///