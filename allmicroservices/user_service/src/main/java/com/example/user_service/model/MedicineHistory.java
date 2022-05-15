package com.example.user_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medicine_history")
public class MedicineHistory {

    @Id
    @Column(name = "history_id")
    private int historyId;

    @Column(name = "med_date")
    private Date date;

    @Column(name = "taken")
    private String taken;

    @Column(name = "not_taken")
    private String notTaken;

    @ManyToOne()
    @JoinColumn(name = "medicine_history", referencedColumnName = "medicine_id")
    @JsonIgnore
    UserMedicines userMedicines;


}
///