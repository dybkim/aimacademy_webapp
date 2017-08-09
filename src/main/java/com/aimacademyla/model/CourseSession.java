package com.aimacademyla.model;

import com.aimacademyla.model.reference.TemporalReference;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by davidkim on 3/1/17.
 */

@Entity(name="Course_Session")
public class CourseSession implements Serializable{

    private static final long serialVersionUID = 2390396075467945893L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CourseSessionID")
    private int courseSessionID;

    @Column(name="CourseID")
    private int courseID;

    @Column(name="CourseSessionDate")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate courseSessionDate;

    @Column(name="NumMembersAttended")
    private int numMembersAttended;

    public int getCourseSessionID() {
        return courseSessionID;
    }

    public void setCourseSessionID(int courseSessionID) {
        this.courseSessionID = courseSessionID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
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
}
