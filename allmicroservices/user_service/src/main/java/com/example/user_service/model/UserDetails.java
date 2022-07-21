package com.example.user_service.model;

import javax.persistence.*;

import javax.validation.constraints.NotBlank;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This is entity class for User details
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_details")
public class UserDetails implements Serializable {
    @Id
    @Column(
            name = "userdet_id",
            nullable = false,
            length = 100
    )
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String userDetId;
    @Column(
            name = "bio",
            columnDefinition = "String(250) default ''",
            length = 100
    )
    private String bio;
    @Column(
            name = "pic_path",
            nullable = false,
            length = 100
    )
    @NotBlank(message = "PicPath is mandatory")
    private String picPath;
    @Column(
            name = "age",
            columnDefinition = "int default 0"
    )
    private int age;
    @Column(
            name = "fcm_token",
            nullable = false,
            length = 200
    )
    private String fcmToken;
    @Column(
            name = "pin_code",
            columnDefinition = "int default 0"
    )
    private int pinCode;
    @Column(
            name = "user_contact",
            columnDefinition = "Long default 0"
    )
    private Long userContact;
    @Column(name = "latitude")
    private float latitude;
    @Column(name = "longitude")
    private float longitude;
    @Column(name = "address")
    private String address;
    @Column(name = "gender")
    private String gender;
    @Column(
            name = "blood_group",
            columnDefinition = "String(250) default ''",
            length = 50
    )
    private String bloodGroup;
    @Column(
            name = "martial_status",
            columnDefinition = "String(250) default ''",
            length = 50
    )
    private String martialStatus;
    @Column(
            name = "weight",
            columnDefinition = "int default 0"
    )
    private int weight;
    @Column(name = "emergency_contact")
    private int emergencyContact;
    @Column(name = "past_medication")
    private String pastMedication;

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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_user_id",
            referencedColumnName = "user_id"
    )
    @JsonIgnore
    private User user;
}


