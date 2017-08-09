package com.aimacademyla.model.reference;

import java.time.LocalDate;
import java.time.Period;

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
        int numMonths = period.getMonths() + (12 * period.getYears());
        return numMonths;
    }
}
