package com.example.user_service.pojos.response.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendImageResponse {


    private String status;
    private String message;

}
