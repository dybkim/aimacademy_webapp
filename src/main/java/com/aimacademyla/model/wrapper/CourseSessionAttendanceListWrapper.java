package com.aimacademyla.model.wrapper;

import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.model.Member;

import java.io.Serializable;
import java.util.*;

/**
 * Created by davidkim on 3/8/17.
 */
public class CourseSessionAttendanceListWrapper implements Serializable{

    private static final long serialVersionUID = -4873233226709888099L;

    private Map<Integer,Attendance> attendanceMap;

    private CourseSession courseSession;

    public CourseSessionAttendanceListWrapper(){
        attendanceMap = new HashMap<>();
    }

    public CourseSessionAttendanceListWrapper(Map<Integer,Attendance> attendanceMap){
        this.attendanceMap = attendanceMap;
    }

    public CourseSessionAttendanceListWrapper(CourseSession courseSession){
        this.courseSession = courseSession;
    }

    public CourseSessionAttendanceListWrapper(CourseSession courseSession, Map<Integer, Attendance> attendanceMap){
        this.attendanceMap = attendanceMap;
        this.courseSession = courseSession;
    }

    public Map<Integer, Attendance> getAttendanceMap() {
        return attendanceMap;
    }

    public void setAttendanceMap(Map<Integer, Attendance> attendanceMap) {
        this.attendanceMap = attendanceMap;
    }

    public CourseSession getCourseSession() {
        return courseSession;
    }

    public void setCourseSession(CourseSession courseSession) {
        this.courseSession = courseSession;
    }
}
