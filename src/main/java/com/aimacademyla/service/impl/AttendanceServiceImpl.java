package com.aimacademyla.service.impl;

import com.aimacademyla.dao.AttendanceDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.*;
import com.aimacademyla.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 3/2/17.
 */

@Service
public class AttendanceServiceImpl extends GenericServiceImpl<Attendance, Integer> implements AttendanceService{

    private AttendanceDAO attendanceDAO;

    private final AIMEntityType AIM_ENTITY_TYPE = AIMEntityType.ATTENDANCE;

    @Autowired
    public AttendanceServiceImpl(@Qualifier("attendanceDAO") GenericDAO<Attendance, Integer> genericDAO){
        super(genericDAO);
        this.attendanceDAO = (AttendanceDAO)genericDAO;
    }

    @Override
    public List<Attendance> getAttendanceForCourse(Course course) {
        return attendanceDAO.getAttendanceForCourse(course);
    }

    @Override
    public List<Attendance> getAttendanceForCourseForDate(Course course, LocalDate date) {
        return attendanceDAO.getAttendanceForCourseForDate(course, date);
    }

    @Override
    public List<Attendance> getAttendanceForCourseSession(CourseSession courseSession) {
        return attendanceDAO.getAttendanceForCourseSession(courseSession.getCourseSessionID());
    }

    @Override
    public List<Attendance> getAttendanceForCourseSession(int courseSessionID){
        return attendanceDAO.getAttendanceForCourseSession(courseSessionID);
    }

    @Override
    public List<List<Attendance>> getAttendanceListsForCourseSessionList(List<CourseSession> courseSessionList){
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
    public List<Attendance> getAttendanceForMemberForCourse(Member member, Course course) {
        return attendanceDAO.getAttendanceForMemberForCourse(member, course);
    }

    @Override
    public Attendance getAttendanceForMemberForCourseForDate(Member member, Course course, LocalDate date) {
        return attendanceDAO.getAttendanceForMemberForCourseForDate(member, course, date);
    }

    @Override
    public void addOrUpdateAttendanceList(List<Attendance> attendanceList){attendanceDAO.addOrUpdateAttendanceList(attendanceList);}

    @Override
    public void remove(List<Attendance> attendanceList){
        for(Attendance attendance : attendanceList)
            remove(attendance);
    }

    @Override
    public AIMEntityType getAIMEntityType(){
        return AIM_ENTITY_TYPE;
    }
}
