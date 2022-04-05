package com.example.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medicine")
public class MedicineEntity {

    @Id
    @Column(name = "med_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private int med_id;

    @Column(name = "med_name")
    private String med_name;



}
///