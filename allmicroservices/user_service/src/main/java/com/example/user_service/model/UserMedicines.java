package com.example.user_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * This is entity class for medicines
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_medicine")
public class UserMedicines implements Serializable {

    @Id
    @Column(name = "medicine_id", nullable = false)
    private int medicineId;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "medicine_name", nullable = false, length = 150)
    private String medicineName;

    @Column(name = "medicine_des", nullable = false, length = 150)
    private String medicineDes;

    @Column(name = "days", nullable = false, length = 200)
    private String days;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "time", nullable = false, length = 200)
    private String time;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "total_med_reminders", nullable = false)
    private int totalMedReminders;

    @Column(name = "current_count", nullable = false)
    private int currentCount;

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


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_med_id",
            referencedColumnName = "user_id"
    )

    @JsonIgnore
    private User userEntity;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "userMedicines"

    )
    @JsonIgnore
    private transient List<MedicineHistory> medicineHistories;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "userMedicines"
    )
    @JsonIgnore
    private transient List<Image> images;

}
