package com.example.user_service.util;

import java.time.LocalDateTime;
/**
 * This is a Date helper class for getting dates and time
 */
public class DateHelper {
    private DateHelper(){}

    public static java.sql.Date getCurrentDate(){
        java.util.Date date=new java.util.Date();

        return new java.sql.Date(date.getTime());

    }


    public static LocalDateTime getCurrentDatetime(){

        return (LocalDateTime.now());

    }

}
