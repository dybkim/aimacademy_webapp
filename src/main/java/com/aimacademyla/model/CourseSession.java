package com.aimacademyla.model;

import com.aimacademyla.model.dto.CourseSessionDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.*;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by davidkim on 3/1/17.
 */

@Entity(name="Course_Session")
public class CourseSession extends AIMEntity implements Serializable{

    private static final long serialVersionUID = 2390396075467945893L;

    public CourseSession(){
        attendanceMap = new HashMap<>();
        numMembersAttended = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CourseSessionID")
    private int courseSessionID;

    @ManyToOne
    @JoinColumn(name="CourseID", referencedColumnName = "CourseID")
    private Course course;

    @Column(name="CourseSessionDate")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate courseSessionDate;

    @Column(name="NumMembersAttended")
    private int numMembersAttended;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "courseSession", orphanRemoval = true)
    @MapKey(name="attendanceID")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<Integer, Attendance> attendanceMap;

    public void addAttendance(Attendance attendance){
        if(attendanceMap == null)
            return;

        if(attendance.getWasPresent())
            numMembersAttended++;

        attendanceMap.put(attendance.getAttendanceID(), attendance);
        attendance.setCourseSession(this);
    }

    public void updateAttendance(Attendance attendance) {
        if (attendanceMap == null)
            return;

        Attendance persistedAttendance = attendanceMap.get(attendance.getAttendanceID());

        if (persistedAttendance == null || !persistedAttendance.getWasPresent()){
            if (attendance.getWasPresent())
                numMembersAttended++;
        }

        else if(persistedAttendance.getWasPresent())
            if(!attendance.getWasPresent())
                numMembersAttended--;

        attendanceMap.put(attendance.getAttendanceID(), attendance);
        attendance.setCourseSession(this);
    }

    public void removeAttendance(Attendance attendance){
        if(attendanceMap == null)
            return;

        Attendance persistedAttendance = attendanceMap.get(attendance.getAttendanceID());
        if(persistedAttendance == null)
            return;

        if(attendance.getWasPresent())
            numMembersAttended--;

        attendanceMap.remove(attendance.getAttendanceID());
    }

    boolean memberWasPresent(Member member){
        for(Attendance attendance : attendanceMap.values()){
            if(attendance.getMember().equals(member) && attendance.getWasPresent())
                return true;
        }
        return false;
    }

    public void addAttendanceList(List<Attendance> attendanceList){
        if(attendanceMap == null)
            attendanceMap = new HashMap<>();

        for(Attendance attendance : attendanceList)
            updateAttendance(attendance);
    }

    @JsonIgnore
    public CourseSessionDTO getCourseSessionDTO(){
        CourseSessionDTO courseSessionDTO = new CourseSessionDTO();
        courseSessionDTO.setCourseSessionID(courseSessionID);
        courseSessionDTO.setCourse(course);
        courseSessionDTO.setCourseSessionDate(courseSessionDate);
        courseSessionDTO.setAttendanceList(new ArrayList<>(attendanceMap.values()));
        return courseSessionDTO;
    }

    /*
     * Have to override equals in order to implement a Set of CourseSessions
     */
    @Override
    public boolean equals(Object object){
        if(this == object)
            return true;

        if(!(object instanceof CourseSession))
            return false;

        CourseSession courseSession = (CourseSession) object;
        return courseSession.getCourseSessionID() == courseSessionID;
    }

    @Override
    public int getBusinessID() {
        return courseSessionID;
    }

    @Override
    public void setBusinessID(int courseSessionID){
        this.courseSessionID = courseSessionID;
    }


    public int getCourseSessionID() {
        return courseSessionID;
    }

    public void setCourseSessionID(int courseSessionID) {
        this.courseSessionID = courseSessionID;
    }

    public LocalDate getCourseSessionDate() {
        return courseSessionDate;
    }

    public void setCourseSessionDate(LocalDate courseSessionDate) {
        this.courseSessionDate = courseSessionDate;
    }

    public int getNumMembersAttended() {
        return numMembersAttended;
    }

    public void setNumMembersAttended(int numMembersAttended) {
        this.numMembersAttended = numMembersAttended;
    }

    private void setNumMembersAttended(){
        if(attendanceMap == null)
            return;

        int count = 0;
        for(Attendance attendance : attendanceMap.values())
            if(attendance.getWasPresent())
                count++;

        numMembersAttended = count;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Map<Integer, Attendance> getAttendanceMap(){
        return attendanceMap;
    }

    public void setAttendanceMap(Map<Integer, Attendance> attendanceMap) {
        this.attendanceMap = attendanceMap;
        setNumMembersAttended();
    }
}
