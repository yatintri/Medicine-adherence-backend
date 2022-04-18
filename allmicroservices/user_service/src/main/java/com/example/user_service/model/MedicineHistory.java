package com.example.user_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medicinehistory")
public class MedicineHistory {

    @Id
    @Column(name = "history_id")
    private int historyId;

    @Column(name = "date")
    private String date;

    @Column(name = "taken")
    private String taken;

    @Column(name = "nottaken")
    private String nottaken;

    @ManyToOne()
    @JoinColumn(name = "medicinehistory", referencedColumnName = "medicine_id")
    @JsonIgnore
    UserMedicines userMedicines;


}
//