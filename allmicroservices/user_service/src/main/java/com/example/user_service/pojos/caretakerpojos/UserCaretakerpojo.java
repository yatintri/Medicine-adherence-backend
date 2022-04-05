package com.example.user_service.pojos.caretakerpojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCaretakerpojo {

    private String patientName;
    private Boolean reqStatus;
    private String caretakerId;
    private String patientId;
    private String caretakerUsername;
    private String createdAt;
    private String sentBy;


}
