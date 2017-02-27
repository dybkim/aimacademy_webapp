package com.aimacademyla.model.composite;

import com.aimacademyla.model.StudentRegistration;

import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by davidkim on 2/13/17.
 */


@Embeddable
public class StudentRegistrationPK implements Serializable {

    private static final long serialVersionUID = -7426421351182880811L;

    private int studentMemberID;

    private int courseID;

    public StudentRegistrationPK(){}

    public StudentRegistrationPK(int studentMemberID, int courseID){
        this.studentMemberID = studentMemberID;
        this.courseID = courseID;
    }

    public int getStudentMemberID() {
        return studentMemberID;
    }

    public void setStudentMemberID(int studentMemberID) {
        this.studentMemberID = studentMemberID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    @Override
    public boolean equals(Object obj) {
        StudentRegistrationPK studentRegistrationPK = (StudentRegistrationPK) obj;
        return (this == obj) || (this.courseID == studentRegistrationPK.getCourseID() && this.studentMemberID == studentRegistrationPK.getStudentMemberID());
    }
}
