package com.example.user_service.pojos.dto.response.caretaker;


import com.example.user_service.model.UserCaretaker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * This is a response class to return the list of caretaker
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaretakerResponse1 {

    private String status;
    private String message;
    private List<UserCaretaker> userCaretakerList;
}
