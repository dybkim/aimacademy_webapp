package com.aimacademyla.model;

import com.aimacademyla.model.enums.AIMEntityType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
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
    @NotEmpty(message = "Must provide first name!")
    @Length(max=30)
    private String employeeFirstName;

    @Column(name="EmployeeLastName")
    @NotEmpty(message="Must provide last name!")
    @Length(max=30)
    private String employeeLastName;

    @Column(name="JobDescription")
    @Length(max=15)
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

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object object){
        if(!(object instanceof Employee))
            throw new IllegalArgumentException("Argument must be of type Employee!");

        Employee employee = (Employee) object;
        return employee.getEmployeeID() == employeeID;
    }
}
