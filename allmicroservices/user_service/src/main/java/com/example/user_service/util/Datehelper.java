package com.example.user_service.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Datehelper {

    public static java.sql.Date getcurrentdate(){
        java.util.Date date=new java.util.Date();

        return new java.sql.Date(date.getTime());

    }

    public static String getDay(){

        String[] days = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
        return days[new Date().getDay()];


    }
    public static LocalDateTime getcurrentdatatime(){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return (now);

    }

}
