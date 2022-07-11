package com.example.user_service.pojos.response.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlErrorResponse {


    private String message;

    private String status;


}
