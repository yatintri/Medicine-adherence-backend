package com.example.user_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
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
    @NotNull(message = "Start date is mandatory")
    private Date startDate;

    @Column(name = "medicine_name")
    @NotNull(message = "Medicine name is mandatory")
    @NotBlank(message = "Medicine name is mandatory")
    private String medicineName;

    @Column(name = "medicine_des")
    @NotNull(message = "Medicine description is mandatory")
    @NotBlank(message = "Medicine description is mandatory")
    private String medicineDes;

    @Column(name = "days")
    @NotNull(message = "Days is mandatory")
    @NotBlank(message = "Days is mandatory")
    private String days;

    @Column(name = "end_date")
    @NotNull(message = "End date is mandatory")
    private Date EndDate;

    @Column(name = "time")
    @NotNull(message = "Time is mandatory")
    @NotBlank(message = "Time is mandatory")
    private String time;

    @Column(name = "title")
    @NotNull(message = "Title is mandatory")
    @NotBlank(message = "Title is mandatory")
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
