package com.example.user_service.pojos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineHistoryDTO {

    private int remId;
    private String date;
    private String[] taken;
    private String[] not_taken;


}
//