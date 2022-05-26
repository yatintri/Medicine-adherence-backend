package com.example.user_service.exception;

import org.springframework.dao.DataAccessException;

public class DataAccessExceptionMessage extends DataAccessException {

    public DataAccessExceptionMessage(String msg) {
        super(msg);
    }
}
