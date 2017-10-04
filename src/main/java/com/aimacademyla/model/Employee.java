package com.aimacademyla.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class Employee implements Serializable{

    private static final long serialVersionUID = 6215673125510544450L;

    @Id
    @Column(name="EmployeeID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int employeeID;

    @Column(name="EmployeeFirstName")
    private String employeeFirstName;

    @Column(name="EmployeeLastName")
    private String employeeLastName;

    @Column(name="JobDescription")
    private String jobDescription;

    @Column(name="DateEmployed")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate dateEmployed;

    @Column(name="DateTerminated")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate dateTerminated;

    @Column(name="IsActive")
    private boolean isActive;

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public LocalDate getDateEmployed() {
        return dateEmployed;
    }

    public void setDateEmployed(LocalDate dateEmployed) {
        this.dateEmployed = dateEmployed;
    }

    public LocalDate getDateTerminated() {
        return dateTerminated;
    }

    public void setDateTerminated(LocalDate dateTerminated) {
        this.dateTerminated = dateTerminated;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
