package com.example.user_service.pojos.response;


import com.example.user_service.model.UserMedicines;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineResponse {
    private String status;
    private String message;
    private List<UserMedicines> userMedicinesList;

}
