package com.example.user_service.pojos.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * This is a Pojo class for Medicine History
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineHistoryDTO {


    private int remId;

    @NotNull(message = "Date is mandatory")
    private Date date;

    private String[] taken;

    private String[] notTaken;


}
