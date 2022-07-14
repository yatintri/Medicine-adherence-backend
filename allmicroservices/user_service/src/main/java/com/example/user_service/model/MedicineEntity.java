package com.example.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/**
 * @Deprecated  This is entity class for Medicine
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medicine")
public class MedicineEntity {

    @Id
    @Column(name = "med_id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private int medId;

    @Column(name = "med_name",nullable = false,length = 150)
    private String medName;



}
