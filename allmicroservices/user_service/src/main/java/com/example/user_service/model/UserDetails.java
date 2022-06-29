package com.example.user_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_details")
@ToString(exclude = "")
public class UserDetails {
    @Id
    @Column(name = "userdet_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String userDetId;

    @Column(name = "bio",columnDefinition = "String(250) default ''")
    private String bio;

    @Column(name = "pic_path")
    @NotBlank(message = "PicPath is mandatory")
    private String picPath;

    @Column(name = "age", columnDefinition = "int default 0")
    private int age;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "pincode",columnDefinition = "int default 0")
    private int pincode;

    @Column(name = "user_contact",columnDefinition = "Long default 0")
    private Long userContact;

    @Column(name = "lattitude")
    private float lattitude;

    @Column(name = "longitude")
    private float longitude;

    @Column(name = "address")
    private String address;

    @Column(name = "gender")
    private String gender;

    @Column(name = "blood_group",columnDefinition = "String(250) default ''")
    private String bloodGroup;

    @Column(name = "martial_status",columnDefinition = "String(250) default ''")
    private String martialStatus;

    @Column(name = "weight", columnDefinition = "int default 0")
    private int weight;

    @Column(name = "emergency_contact")
    private int emergencyContact;

    @Column(name = "past_medication")
    private String pastMedication;

    @OneToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "user_user_id",
            referencedColumnName = "user_id"
    )
    @JsonIgnore
    private UserEntity user;
}
