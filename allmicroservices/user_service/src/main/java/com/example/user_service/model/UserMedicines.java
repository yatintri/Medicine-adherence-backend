package com.example.user_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_medicine")
public class UserMedicines {

    @Id
    @Column(name = "medicine_id",nullable = false)
    private int medicineId;

    @Column(name = "start_date",nullable = false)
    private Date startDate;

    @Column(name = "medicine_name",nullable = false,length = 150)
    private String medicineName;

    @Column(name = "medicine_des",nullable = false,length = 150)
    private String medicineDes;

    @Column(name = "days",nullable = false,length = 200)
    private String days;

    @Column(name = "end_date",nullable = false)
    private Date endDate;

    @Column(name = "time",nullable = false,length = 200)
    private String time;

    @Column(name = "title",nullable = false,length = 150)
    private String title;

    @Column(name = "total_med_reminders",nullable = false)
    private int totalMedReminders;

    @Column(name = "current_count",nullable = false)
    private int currentCount;


    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(
            name = "user_med_id",
            referencedColumnName = "user_id"
    )

    @JsonIgnore
    private UserEntity userEntity;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "userMedicines"

    )
    @JsonIgnore
    private List<MedicineHistory> medicineHistories;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "userMedicines"
    )
    @JsonIgnore
    private List<Image> images;

}
