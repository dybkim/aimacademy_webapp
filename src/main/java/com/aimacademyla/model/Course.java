package com.aimacademyla.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Course Entity represents one academic program held during a specific time period
 *
 * Created by davidkim on 2/8/17.
 */

@Entity
public class Course implements Serializable{

    private static final long serialVersionUID = 3942567537260692323L;

    public static final int OPEN_STUDY_ID = 2;
    public static final int OTHER_ID = 1;

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
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal pricePerBillableUnit;

    @Column(name="ClassDuration")
    private BigDecimal classDuration;

    @Column(name="BillableUnitDuration")
    private BigDecimal billableUnitDuration;

    //Primary Instructor ID
    @Column(name="EmployeeID")
    private Integer employeeID;

    @Column(name="BillableUnitType")
    private String billableUnitType;

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

    public BigDecimal getPricePerBillableUnit() {
        return pricePerBillableUnit;
    }

    public void setPricePerBillableUnit(BigDecimal pricePerBillableUnit) {
        this.pricePerBillableUnit = pricePerBillableUnit;
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

    public BigDecimal getBillableUnitDuration() {
        return billableUnitDuration;
    }

    public void setBillableUnitDuration(BigDecimal billableUnitDuration) {
        this.billableUnitDuration = billableUnitDuration;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public BigDecimal getClassDuration() {
        return classDuration;
    }

    public void setClassDuration(BigDecimal classDuration) {
        this.classDuration = classDuration;
    }

    public String getBillableUnitType() {
        return billableUnitType;
    }

    public void setBillableUnitType(String billableUnitType) {
        this.billableUnitType = billableUnitType;
    }
}
