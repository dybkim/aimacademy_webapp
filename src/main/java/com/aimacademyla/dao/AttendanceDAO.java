package com.aimacademyla.dao;

import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.model.Member;

import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 3/2/17.
 */
public interface AttendanceDAO {

    List<Attendance> getAttendanceForCourse(Course course);
    List<Attendance> getAttendanceForCourse(Course course, Date date);

    List<Attendance> getAttendanceForCourseSession(CourseSession courseSession);

    List<Attendance> getAttendanceForMember(Member member);
    List<Attendance> getAttendanceForMember(Member member, Course course);
    List<Attendance> getAttendanceForMember(Member member, Course course, Date date);

    void addAttendance(Attendance attendance);

    void editAttendance(Attendance attendance);

    void deleteAttendance(Attendance attendance);
}
