package com.aimacademyla.model;

import com.aimacademyla.model.composite.InstructorCourseRegistrationPK;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name="Instructor_Course_Registration")
public class InstructorCourseRegistration implements Serializable{

    private static final long serialVersionUID = 6943663109872941964L;

    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private InstructorCourseRegistrationPK instructorCourseRegistrationPK;

    @MapsId("EmployeeID")
    @Column(insertable=false, updatable=false)
    private int employeeID;

    @MapsId("CourseID")
    @Column(insertable=false, updatable=false)
    private int courseID;

    @Column(name="HourlyRate")
    private BigDecimal hourlyRate;

    @Column(name="DateRegistered")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate dateRegistered;

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public LocalDate getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(LocalDate dateRegistered) {
        this.dateRegistered = dateRegistered;
    }
}
