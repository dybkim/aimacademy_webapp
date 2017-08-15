package com.aimacademyla.formatter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class LocalDateFormatter implements Formatter<LocalDate> {

    private final DateTimeFormatter formatter;

    public LocalDateFormatter() {
        formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    }

    public LocalDateFormatter(String dateFormat) {
        formatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public LocalDate parse(String s, Locale locale) throws ParseException {
        return LocalDate.parse(s, formatter);
    }

    @Override
    public String print(LocalDate localDate, Locale locale) {
        if(localDate == null)
            return "";

        return localDate.format(formatter);
    }
}
