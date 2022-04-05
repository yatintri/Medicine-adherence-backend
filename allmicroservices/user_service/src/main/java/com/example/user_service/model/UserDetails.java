package com.example.user_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "userdetails")
@ToString(exclude = "")
public class UserDetails {
    @Id
    @Column(name = "userdet_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String userdetId;

    @Column(name = "bio")
    private String bio;

    @Column(name = "pic_path")
    private String picPath;

    @Column(name = "age")
    private int age;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "pincode")
    private int pincode;

    @Column(name = "usercontact")
    private Long usercontact;

    @Column(name = "lattitude")
    private float lattitude;

    @Column(name = "longitude")
    private float longitude;

    @Column(name = "address")
    private String address;

    @Column(name = "gender")
    private String gender;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "martial_status")
    private String martialStatus;

    @Column(name = "weight")
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
//