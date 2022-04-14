package com.example.user_service.pojos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCaretakerDTO {

    private String patientName;
    private Boolean reqStatus;
    private String caretakerId;
    private String patientId;
    private String caretakerUsername;
    private String createdAt;
    private String sentBy;

}
