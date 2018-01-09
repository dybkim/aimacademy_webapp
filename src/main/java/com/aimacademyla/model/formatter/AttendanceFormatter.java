package com.aimacademyla.model.formatter;

import com.aimacademyla.dao.AttendanceDAO;
import com.aimacademyla.model.Attendance;
import com.aimacademyla.service.AttendanceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class AttendanceFormatter implements Formatter<Attendance> {

    private AttendanceService attendanceService;

    private static final Logger logger = LogManager.getLogger(AttendanceFormatter.class.getName());

    @Autowired
    public AttendanceFormatter(AttendanceService attendanceService){
        logger.debug("attendanceFormatter initialized");
        this.attendanceService = attendanceService;
    }

    @Override
    public String print(Attendance attendance, Locale locale) {
        return "Attendance: " + attendance.getMember().getMemberFirstName() + " " +attendance.getMember().getMemberLastName() + ", " +
                attendance.getCourseSession() + " on " + attendance.getAttendanceDate().toString();
    }

    @Override
    public Attendance parse(String attendanceID, Locale locale) throws ParseException {
        logger.debug("parsing attendanceID: " + attendanceID);
        return attendanceService.get(Integer.parseInt(attendanceID));
    }
}
