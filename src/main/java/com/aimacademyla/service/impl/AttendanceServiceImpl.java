package com.aimacademyla.service.impl;

import com.aimacademyla.dao.AttendanceDAO;
import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.model.Member;
import com.aimacademyla.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 3/2/17.
 */

@Service
public class AttendanceServiceImpl implements AttendanceService{

    private AttendanceDAO attendanceDAO;

    @Autowired
    public AttendanceServiceImpl(AttendanceDAO attendanceDAO){
        this.attendanceDAO = attendanceDAO;
    }

    @Override
    public List<Attendance> getAttendanceForCourse(Course course) {
        return attendanceDAO.getAttendanceForCourse(course);
    }

    @Override
    public List<Attendance> getAttendanceForCourse(Course course, Date date) {
        return attendanceDAO.getAttendanceForCourse(course, date);
    }

    @Override
    public List<Attendance> getAttendanceForCourseSession(CourseSession courseSession) {
        return attendanceDAO.getAttendanceForCourseSession(courseSession);
    }

    @Override
    public List<List<Attendance>> getAttendanceListForCourseSessionList(List<CourseSession> courseSessionList){
        List<List<Attendance>> attendanceListList = new ArrayList<>();
        for(CourseSession courseSession : courseSessionList){
            attendanceListList.add(getAttendanceForCourseSession(courseSession));
        }

        return attendanceListList;
    }

    @Override
    public List<Attendance> getAttendanceForMember(Member member) {
        return attendanceDAO.getAttendanceForMember(member);
    }

    @Override
    public List<Attendance> getAttendanceForMember(Member member, Course course) {
        return attendanceDAO.getAttendanceForMember(member, course);
    }

    @Override
    public List<Attendance> getAttendanceForMember(Member member, Course course, Date date) {
        return attendanceDAO.getAttendanceForMember(member, course, date);
    }

    @Override
    public void addAttendance(Attendance attendance) {
        attendanceDAO.addAttendance(attendance);
    }

    @Override
    public void editAttendance(Attendance attendance) {
        attendanceDAO.editAttendance(attendance);
    }

    @Override
    public void deleteAttendance(Attendance attendance) {
        attendanceDAO.deleteAttendance(attendance);
    }
}
