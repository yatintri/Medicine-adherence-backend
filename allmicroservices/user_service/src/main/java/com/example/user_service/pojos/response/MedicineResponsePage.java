package com.example.user_service.pojos.response;

import com.example.user_service.model.MedicineHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineResponsePage {

    private String status;
    private String message;
    private long totalItems;
    private int totalPage;
    private int currentPage;
    private Stream<MedicineHistory> userMedicinesList;
}
