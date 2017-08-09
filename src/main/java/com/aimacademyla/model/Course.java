package com.aimacademyla.model;

import com.aimacademyla.model.reference.TemporalReference;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Course Entity represents one academic program held during a specific time period
 *
 * Created by davidkim on 2/8/17.
 */

@Entity
public class Course implements Serializable{

    private static final long serialVersionUID = 3942567537260692323L;

    public static final int OPEN_STUDY_ID = 8;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CourseID")
    private int courseID;

    @Column(name="CourseName")
    @NotEmpty(message = "Must provide course title")
    private String courseName;

    @Column(name="CourseStartDate")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate courseStartDate;

    @Column(name="CourseEndDate")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate courseEndDate;

    @Column(name="IsActive")
    private boolean isActive;

    @Column(name="CourseType")
    @NotEmpty(message = "Must provide course type")
    private String courseType;

    @Column(name="NumEnrolled")
    private int numEnrolled;

    @Column(name="TotalNumSessions")
    private int totalNumSessions;

    @Column(name="SeasonID")
    private int seasonID;

    @Column(name="PricePerHour")
    private double pricePerHour;

    @Column(name="ClassDuration")
    private double classDuration;

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

    public LocalDate getCourseStartDate() {
        return courseStartDate;
    }

    public void setCourseStartDate(LocalDate courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public LocalDate getCourseEndDate() {
        return courseEndDate;
    }

    public void setCourseEndDate(LocalDate courseEndDate) {
        this.courseEndDate = courseEndDate;
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

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getTotalNumSessions() {
        return totalNumSessions;
    }

    public void setTotalNumSessions(int totalNumSessions) {
        this.totalNumSessions = totalNumSessions;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(int seasonID) {
        this.seasonID = seasonID;
    }

    public double getClassDuration() {
        return classDuration;
    }

    public void setClassDuration(double classDuration) {
        this.classDuration = classDuration;
    }
}
