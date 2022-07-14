package com.example.user_service.pojos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
/**
 * This is a Pojo class for User details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {

    @NotNull(message = "bio is mandatory")
    @NotBlank(message = "bio is mandatory")
    private String bio;

    @Min(15)
    @Max(120)
    private int age;

    @Min(10)
    private Long userContact;

    @NotNull(message = "Gender is mandatory")
    @NotBlank(message = "Gender is mandatory")
    private String gender;

    @NotNull(message = "blood group is mandatory")
    @NotBlank(message = "blood group is mandatory")
    private String bloodGroup;

    @NotNull(message = "martial status is mandatory")
    @NotBlank(message = "martial status is mandatory")
    private String martialStatus;

    @Min(45)
    @Max(200)
    private int weight;
}
