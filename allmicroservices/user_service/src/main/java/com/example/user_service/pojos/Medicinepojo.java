package com.example.user_service.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicinepojo {

    private int user_id;
    private String days;
    private int current_count;
    private String end_date;
    private String medicine_des;
    private int total_med_reminders;
    private String medicine_name;
    private String title;
    private String start_date;
    private int status;
    private String time;



//
}
