package com.example.user_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_medicines")
public class UserMedicines {

    @Id
    @Column(name = "medicine_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String medicine_id;

    @Column(name = "create_time")
    private String create_time;

    @Column(name = "medicine_name")
    private String medicine_name;

    @Column(name = "medicine_type")
    private String medicine_type;

    @Column(name = "active_status")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean active_status;


    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "user_med_id",
            referencedColumnName = "user_id"
    )

    @JsonIgnore
    private UserEntity userEntity;


}
//
