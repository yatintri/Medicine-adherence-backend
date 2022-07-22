package com.example.user_service.pojos.response.user;

import com.example.user_service.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailResponse implements Serializable {

    private String status;
    private String message;
    private User user;
}
