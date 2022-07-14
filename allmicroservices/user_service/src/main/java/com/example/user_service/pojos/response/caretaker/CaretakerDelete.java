package com.example.user_service.pojos.response.caretaker;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * This is Response class for Caretaker delete
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaretakerDelete {


    private String status;

    private String message;
}
