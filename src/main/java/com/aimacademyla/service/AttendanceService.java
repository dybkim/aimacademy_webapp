package com.aimacademyla.service;

import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.model.Member;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 3/2/17.
 */
public interface AttendanceService extends GenericService<Attendance, Integer>{

    List<Attendance> getAttendanceForCourse(Course course);
    List<Attendance> getAttendanceForCourseForDate(Course course, LocalDate date);

    List<Attendance> getAttendanceForCourseSession(CourseSession courseSession);
    List<Attendance> getAttendanceForCourseSession(int courseSessionID);
    List<List<Attendance>> getAttendanceListsForCourseSessionList(List<CourseSession> courseSessionList);

    List<Attendance> getAttendanceForMember(Member member);
    List<Attendance> getAttendanceForMemberForCourse(Member member, Course course);
    Attendance getAttendanceForMemberForCourseForDate(Member member, Course course, LocalDate date);

    void addOrUpdateAttendanceList(List<Attendance> attendanceList);
    void remove(List<Attendance> attendanceList);

}
