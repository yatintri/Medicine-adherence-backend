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
    private String userdet_id;

    @Column(name = "bio")
    private String bio;

    @Column(name = "pic_path")
    private String pic_path;

    @Column(name = "age")
    private int age;

    @Column(name = "fcm_token")
    private String fcm_token;

    @Column(name = "pincode")
    private int pincode;

    @Column(name = "usercontact")
    private int usercontact;

    @Column(name = "lattitude")
    private float lattitude;

    @Column(name = "longitude")
    private float longitude;

    @Column(name = "address")
    private String address;

    @Column(name = "gender")
    private String gender;

    @Column(name = "blood_group")
    private String blood_group;

    @Column(name = "martial_status")
    private String martial_status;

    @Column(name = "weight")
    private int weight;

    @Column(name = "emergency_contact")
    private int emergency_contact;

    @Column(name = "past_medication")
    private String past_medication;

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
