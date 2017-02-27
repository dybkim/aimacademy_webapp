package com.aimacademyla.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by davidkim on 2/16/17.
 */
public class PacificTimeDateOffsetter {

    private static SimpleDateFormat dateFormatter = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss");

    static{
        dateFormatter.setTimeZone(TimeZone.getTimeZone("PST"));
    }

    public static Date offsetDateWithPacificTime(Date date){
        try {
            return dateFormatter.parse(date.toString());
        }catch(ParseException e){
            System.out.println("Date input has been entered in the incorrect format");
        }

        return date;
    }
}
