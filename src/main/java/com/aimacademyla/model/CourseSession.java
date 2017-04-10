package com.aimacademyla.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

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
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private Date courseSessionDate;

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

    public Date getCourseSessionDate() {
        return courseSessionDate;
    }

    public void setCourseSessionDate(Date courseSessionDate) {
        this.courseSessionDate = courseSessionDate;
    }

    public int getNumMembersAttended() {
        return numMembersAttended;
    }

    public void setNumMembersAttended(int numMembersAttended) {
        this.numMembersAttended = numMembersAttended;
    }
}
