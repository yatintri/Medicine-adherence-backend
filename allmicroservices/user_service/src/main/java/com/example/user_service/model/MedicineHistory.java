package com.example.user_service.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is entity class for Medicine History
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medicine_history")
public class MedicineHistory implements Serializable {
    @Id
    @Column(
            name = "history_id",
            nullable = false
    )
    private int historyId;
    @Column(
            name = "med_date",
            nullable = false
    )
    @NotNull(message = "Date is mandatory")
    private Date date;
    @Column(
            name = "taken",
            nullable = false,
            length = 50
    )
    private String taken;
    @Column(
            name = "not_taken",
            nullable = false,
            length = 50
    )
    private String notTaken;
    @ManyToOne()
    @JoinColumn(
            name = "medicine_history",
            referencedColumnName = "medicine_id"
    )
    @JsonIgnore
    transient UserMedicines userMedicines;
}

