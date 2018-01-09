package com.aimacademyla.model.dto;

import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by davidkim on 3/8/17.
 */
public class CourseSessionDTO implements Serializable{

    private static final long serialVersionUID = -4873233226709888099L;

    private Integer courseSessionID;
    private List<Attendance> attendanceList;
    private Course course;

    public CourseSessionDTO(){
        courseSessionID = 0;
        attendanceList = new ArrayList<>();
    }

    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate courseSessionDate;

    public Integer getCourseSessionID() {
        return courseSessionID;
    }

    public void setCourseSessionID(Integer courseSessionID) {
        this.courseSessionID = courseSessionID;
    }

    public List<Attendance> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getCourseSessionDate() {
        return courseSessionDate;
    }

    public void setCourseSessionDate(LocalDate courseSessionDate) {
        this.courseSessionDate = courseSessionDate;
    }

    public CourseSession getCourseSession(){
        CourseSession courseSession = new CourseSession();
        courseSession.setCourseSessionID(courseSessionID);
        courseSession.setCourse(course);
        courseSession.setCourseSessionDate(courseSessionDate);
        return courseSession;
    }
}
