package com.example.user_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_medicine")
public class UserMedicines {

    @Id
    @Column(name = "medicine_id")
    private int medicineId;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "medicine_name")
    private String medicineName;

    @Column(name = "medicine_des")
    private String medicineDes;

    @Column(name = "days")
    private String days;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "time")
    private String time;

    @Column(name = "title")
    private String title;

    @Column(name = "total_med_reminders")
    private int totalMedReminders;

    @Column(name = "current_count")
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
///
