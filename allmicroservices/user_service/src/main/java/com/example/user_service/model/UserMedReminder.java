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
@Table(name = "user_med_reminder")
public class UserMedReminder {

    @Id
    @Column(name = "reminder_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String reminderId;

    @Column(name = "reminder_title")
    private String reminderTitle;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "everyday")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean everyday;

    @Column(name = "reminder_status")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean reminderStatus;

    @Column(name = "days")
    private String days;
///
    @Column(name = "reminder_time")
    private String reminderTime;

    @OneToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "med_rem_id",
            referencedColumnName = "medicine_id"
    )
    @JsonIgnore
    private UserMedicines userRem;
}
