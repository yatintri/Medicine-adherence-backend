package com.example.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medicine")
public class MedicineEntity {

    @Id
    @Column(name = "med_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private int medId;

    @Column(name = "med_name")
    @NotNull(message = "Med name is mandatory")
    @NotBlank(message = "Med name is mandatory")
    private String medName;



}
