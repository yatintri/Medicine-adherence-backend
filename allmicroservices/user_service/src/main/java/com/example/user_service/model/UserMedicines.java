package com.example.user_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_medicine")
public class UserMedicines {

    @Id
    @Column(name = "medicine_id")
    private int medicine_id;

    @Column(name = "start_date")
    private String start_date;

    @Column(name = "medicine_name")
    private String medicine_name;

    @Column(name = "medicine_des")
    private String medicine_des;

    @Column(name = "days")
    private String days;

    @Column(name = "end_date")
    private String end_date;

    @Column(name = "time")
    private String time;

    @Column(name = "title")
    private String title;

    @Column(name = "total_med_reminders")
    private int total_med_reminders;

    @Column(name = "current_count")
    private int current_count;



    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "user_med_id",
            referencedColumnName = "user_id"
    )

    @JsonIgnore
    private UserEntity userEntity;

    @OneToOne(
            cascade = CascadeType.ALL,
            mappedBy = "user_rem",
            fetch = FetchType.LAZY
    )
    private UserMedReminder reminderEntity;

}
///
