package com.example.user_service.util;

import java.time.LocalDateTime;

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
