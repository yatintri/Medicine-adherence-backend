package com.example.user_service.pojos.dto.response.medicine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * This is a response class for sync
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncResponse {


    private String status;
    private String message;

}
