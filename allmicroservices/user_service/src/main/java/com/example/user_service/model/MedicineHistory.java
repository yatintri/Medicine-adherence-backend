package com.example.user_service.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.*;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

/**
 * This is entity class for Medicine History
 */
@Getter
@Setter
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
            name = "medicine_date",
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
    @ManyToOne
    @JoinColumn(
            name = "medicine_history",
            referencedColumnName = "medicine_id"
    )
    @JsonIgnore
    private UserMedicines userMedicines;
}

