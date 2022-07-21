package com.example.user_service.model;

import javax.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @Deprecated This is entity class for Medicine
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medicine")
public class Medicine {
    @Id
    @Column(
            name = "med_id",
            nullable = false
    )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int medId;
    @Column(
            name = "med_name",
            nullable = false,
            length = 150
    )
    private String medName;

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
}

