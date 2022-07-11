package com.example.user_service.pojos.response.caretaker;

import com.example.user_service.model.UserCaretaker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaretakerResponse {

    private String status;

    private String message;

    private UserCaretaker userCaretaker;
}
