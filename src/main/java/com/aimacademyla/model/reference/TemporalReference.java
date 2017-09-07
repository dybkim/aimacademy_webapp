package com.aimacademyla.model.reference;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public enum TemporalReference {

    START_DATE(LocalDate.of(2016,1,1));

    private LocalDate date;

    TemporalReference(LocalDate date){
        this.date = date;
    }

    public LocalDate getDate(){
        return date;
    }

    public static int numMonthsFromInception(){
        return numMonthsFromDate(LocalDate.now());
    }

    public static int numMonthsFromDate(LocalDate date){
        Period period = Period.between(START_DATE.getDate(), date);
        return period.getMonths() + (12 * period.getYears());
    }

    public static HashMap<Integer, LocalDate> getMonthIndexHashMap(LocalDate date){
        int numMonths = numMonthsFromDate(date);
        HashMap<Integer, LocalDate> monthIndexHashMap = new HashMap<>();

        LocalDate startDate = START_DATE.getDate();

        for(int index = 0; index <= numMonths; index++) {
            int years = index / 12;
            int months = index % 12;


            LocalDate localDate = LocalDate.of(startDate.getYear() + years, startDate.getMonthValue() + months, 1);

            monthIndexHashMap.put(index, localDate);
        }

        return monthIndexHashMap;
    }

    public static List<LocalDate> getMonthList(LocalDate date){
        int numMonths = numMonthsFromDate(date);
        List<LocalDate> monthList = new ArrayList<>();

        LocalDate startDate = START_DATE.getDate();

        for(int count = 0; count <= numMonths; count++){
            int years = count / 12;
            int months = count % 12;


            LocalDate localDate = LocalDate.of(startDate.getYear() + years, startDate.getMonthValue() + months, 1);
            monthList.add(localDate);
        }

        return monthList;
    }

    public static List<LocalDate> getMonthList(){
        return getMonthList(LocalDate.now());
    }
}
