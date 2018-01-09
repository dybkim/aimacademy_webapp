package com.aimacademyla.model.builder.entity;

import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.builder.GenericBuilder;

import java.time.LocalDate;

public class AttendanceBuilder implements GenericBuilder<Attendance> {

    private Member member;
    private CourseSession courseSession;
    private boolean wasPresent;
    private LocalDate attendanceDate;
    private int attendanceID;

    public AttendanceBuilder setMember(Member member){
        this.member = member;
        return this;
    }

    public AttendanceBuilder setCourseSession(CourseSession courseSession){
        this.courseSession = courseSession;
        return this;
    }

    public AttendanceBuilder setWasPresent(boolean wasPresent){
        this.wasPresent = wasPresent;
        return this;
    }

    public AttendanceBuilder setAttendanceDate(LocalDate attendanceDate){
        this.attendanceDate = attendanceDate;
        return this;
    }

    public AttendanceBuilder setAttendanceID(int attendanceID){
        this.attendanceID = attendanceID;
        return this;
    }

    @Override
    public Attendance build() {
        Attendance attendance = new Attendance();
        attendance.setMember(member);
        attendance.setCourseSession(courseSession);
        attendance.setWasPresent(wasPresent);
        attendance.setAttendanceDate(attendanceDate);
        attendance.setAttendanceID(attendanceID);
        return attendance;
    }
}
