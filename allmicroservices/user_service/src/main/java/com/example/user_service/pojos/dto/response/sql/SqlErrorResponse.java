package com.example.user_service.pojos.dto.response.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * This is a response class for Sql errors
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlErrorResponse {


    private String message;

    private String status;


}
