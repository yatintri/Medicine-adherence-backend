package com.example.user_service.pojos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicinePojo {

    private int userId;
    private String days;
    private int currentCount;
    private String endDate;
    private String medicineDes;
    private int totalMedReminders;
    private String medicineName;
    private String title;
    private String startDate;
    private int status;
    private String time;



///
}
