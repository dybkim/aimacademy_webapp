package com.aimacademyla.model.composite;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by davidkim on 2/13/17.
 */


@Embeddable
public class MemberCourseRegistrationPK implements Serializable {

    private static final long serialVersionUID = -7426421351182880811L;

    private int memberID;

    private int courseID;

    public MemberCourseRegistrationPK(){}

    public MemberCourseRegistrationPK(int memberID, int courseID){
        this.memberID = memberID;
        this.courseID = courseID;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setmemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    @Override
    public boolean equals(Object obj) {
        MemberCourseRegistrationPK memberCourseRegistrationPK = (MemberCourseRegistrationPK) obj;
        return (this == obj) || (this.courseID == memberCourseRegistrationPK.getCourseID() && this.memberID == memberCourseRegistrationPK.getMemberID());
    }
}
