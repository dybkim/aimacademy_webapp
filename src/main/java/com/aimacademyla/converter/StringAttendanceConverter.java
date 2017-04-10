package com.aimacademyla.converter;

import com.aimacademyla.model.Attendance;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by davidkim on 4/3/17.
 */
public class StringAttendanceConverter implements Converter<String, Attendance> {

    @Override
    public Attendance convert(String s) {
        return null;
    }
}
