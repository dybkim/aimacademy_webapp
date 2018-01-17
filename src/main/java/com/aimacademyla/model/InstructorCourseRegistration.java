package com.aimacademyla.model;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name="Instructor_Course_Registration")
public class InstructorCourseRegistration extends AIMEntity implements Serializable{

    private static final long serialVersionUID = 6943663109872941964L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="InstructorCourseRegistrationID")
    private int instructorCourseRegistrationID;

    @ManyToOne
    @JoinColumn(name="EmployeeID", referencedColumnName = "EmployeeID")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name="CourseID", referencedColumnName = "CourseID")
    private Course course;

    @Column(name="HourlyRate")
    private BigDecimal hourlyRate;

    @Column(name="DateRegistered")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate dateRegistered;

    @Override
    public int getBusinessID() {
        return instructorCourseRegistrationID;
    }

    @Override
    public void setBusinessID(int instructorCourseRegistrationID){
        this.instructorCourseRegistrationID = instructorCourseRegistrationID;
    }

    public int getInstructorCourseRegistrationID() {
        return instructorCourseRegistrationID;
    }

    public void setInstructorCourseRegistrationID(int instructorCourseRegistrationID) {
        this.instructorCourseRegistrationID = instructorCourseRegistrationID;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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
