package com.example.user_service.pojos.response;

import com.example.user_service.model.UserCaretaker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaretakerResponse {

    private String status;

    private String message;

    private UserCaretaker userCaretaker;
}
