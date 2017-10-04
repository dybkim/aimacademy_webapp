package com.aimacademyla.model.composite;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class InstructorCourseRegistrationPK implements Serializable{

    private static final long serialVersionUID = -2267059347228981855L;

    private int memberID;

    private int courseID;

    public InstructorCourseRegistrationPK(int memberID, int courseID){
        this.memberID = memberID;
        this.courseID = courseID;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    @Override
    public boolean equals(Object object){
        InstructorCourseRegistrationPK instructorCourseRegistrationPK = (InstructorCourseRegistrationPK) object;
        return (this == object) || (this.memberID == instructorCourseRegistrationPK.memberID && this.courseID == instructorCourseRegistrationPK.courseID);
    }
}
