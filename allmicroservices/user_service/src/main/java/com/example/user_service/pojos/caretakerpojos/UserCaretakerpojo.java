package com.example.user_service.pojos.caretakerpojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCaretakerpojo {

    private String patient_name;
    private Boolean req_status;
    private String caretaker_id;
    private String patient_id;
    private String caretaker_username;
    private String created_at;
    private String sent_by;


}
