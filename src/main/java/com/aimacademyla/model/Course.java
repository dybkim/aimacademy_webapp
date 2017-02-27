package com.aimacademyla.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by davidkim on 2/8/17.
 */

@Entity
public class Course implements Serializable{

    private static final long serialVersionUID = 3942567537260692323L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CourseID")
    private int courseID;

    @Column(name="CourseName")
    @NotEmpty(message = "Must provide course title")
    private String courseName;

    @Column(name="CourseStartDate")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private Date courseStartDate;

    @Column(name="CourseEndDate")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private Date courseEndDate;

    @Column(name="IsActive")
    private boolean isActive;

    @Column(name="CourseType")
    @NotEmpty(message = "Must provide course type")
    private String courseType;

    @Column(name="NumEnrolled")
    private int numEnrolled;

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Date getCourseStartDate() {
        return courseStartDate;
    }

    public void setCourseStartDate(Date courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public Date getCourseEndDate() {
        return courseEndDate;
    }

    public void setCourseEndDate(Date courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public int getNumEnrolled() {
        return numEnrolled;
    }

    public void setNumEnrolled(int numEnrolled) {
        this.numEnrolled = numEnrolled;
    }
}
