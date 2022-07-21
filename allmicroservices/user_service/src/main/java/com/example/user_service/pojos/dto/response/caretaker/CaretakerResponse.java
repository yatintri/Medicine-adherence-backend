package com.example.user_service.pojos.dto.response.caretaker;

import com.example.user_service.model.UserCaretaker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * This is a response class for Caretaker
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaretakerResponse {

    private String status;

    private String message;

    private UserCaretaker userCaretaker;
}
