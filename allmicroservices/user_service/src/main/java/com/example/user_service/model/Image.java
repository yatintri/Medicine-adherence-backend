package com.example.user_service.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This is entity class for image
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "image")
public class Image implements Serializable {
    @Id
    @Column(
            name = "image_id",
            nullable = false,
            length = 100
    )
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String imageId;
    @Column(
            name = "date",
            nullable = false
    )
    @Temporal(TemporalType.DATE)
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd"
    )
    private Date date;
    @Column(
            name = "time",
            nullable = false,
            length = 50
    )
    private String time;
    @Column(
            name = "caretaker_name",
            nullable = false,
            length = 100
    )
    private String caretakerName;
    @Column(
            name = "image_url",
            nullable = false,
            length = 150
    )
    private String imageUrl;

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "med_image_id",
            referencedColumnName = "medicine_id"
    )
    @JsonIgnore
    UserMedicines userMedicines;
}
