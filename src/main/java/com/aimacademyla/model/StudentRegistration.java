package com.aimacademyla.model;

import com.aimacademyla.model.composite.StudentRegistrationPK;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by davidkim on 2/9/17.
 */

@Entity(name="Student_Registration")
public class StudentRegistration implements Serializable{

    private static final long serialVersionUID = 4571615710752919588L;

    @EmbeddedId
    private StudentRegistrationPK studentRegistrationPK;

    @Column(name="StudentMemberID")
    private Integer studentMemberID;

    @Column(name="ReferStudentMemberID")
    private Integer referStudentMemberID;

    @Column(name="CourseID")
    private int courseID;

    @Column(name="DateRegistered")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private Date dateRegistered;

    public StudentRegistrationPK getStudentRegistrationPK() {
        return studentRegistrationPK;
    }

    public void setStudentRegistrationPK(StudentRegistrationPK studentRegistrationPK) {
        this.studentRegistrationPK = studentRegistrationPK;
    }

    public Integer getStudentMemberID(){return studentMemberID;}

    public void setStudentMemberID(Integer studentMemberID){this.studentMemberID = studentMemberID;}

    public Integer getReferStudentMemberID() {
        return referStudentMemberID;
    }

    public void setReferStudentMemberID(int referStudentMemberID) {
        this.referStudentMemberID = referStudentMemberID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public Date getDateRegistered(){return dateRegistered;}

    public void setDateRegistered(Date dateRegistered){this.dateRegistered = dateRegistered;}
}
