package com.example.user_service.pojos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
/**
 * This is a Pojo class for medicine
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicinePojo {


    private int userId;

    @NotBlank(message = "Days is mandatory")
    @NotNull(message = "Days is mandatory")
    private String days;

    private int currentCount;

    @NotNull(message = "EndDate is mandatory")
    private  Date endDate;

    @NotBlank(message = "Description is mandatory")
    @NotNull(message = "Description is mandatory")
    private String medicineDes;

    private int totalMedReminders;

    @NotBlank(message = "Medicine name is mandatory")
    @NotNull(message = "Medicine name is mandatory")
    private String medicineName;

    @NotBlank(message = "Title is mandatory")
    @NotNull(message = "Title is mandatory")
    private String title;

    @NotNull(message = "StartDate is mandatory")
    private Date startDate;

    private int status;

    @NotBlank(message = "Time is mandatory")
    @NotNull(message = "Time is mandatory")
    private String time;



///
}
