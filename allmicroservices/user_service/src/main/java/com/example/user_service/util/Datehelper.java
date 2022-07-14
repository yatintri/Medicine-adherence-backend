package com.example.user_service.util;

import java.time.LocalDateTime;
/**
 * This is a Date helper class for getting dates and time
 */
public class Datehelper {
    private Datehelper(){}

    public static java.sql.Date getcurrentdate(){
        java.util.Date date=new java.util.Date();

        return new java.sql.Date(date.getTime());

    }


    public static LocalDateTime getcurrentdatatime(){

        return (LocalDateTime.now());

    }

}
