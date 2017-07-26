package com.aimacademyla.model;

import com.aimacademyla.model.composite.MemberCourseRegistrationPK;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by davidkim on 2/9/17.
 */

@Entity(name="Member_Course_Registration")
public class MemberCourseRegistration implements Serializable{

    private static final long serialVersionUID = 4571615710752919588L;

    @EmbeddedId
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private MemberCourseRegistrationPK memberCourseRegistrationPK;

    @MapsId("MemberID")
    @Column(insertable=false, updatable=false)
    private Integer memberID;

    @MapsId("CourseID")
    @Column(insertable=false, updatable=false)
    private int courseID;

    @Column(name="ReferMemberID")
    private Integer referMemberID;

    @Column(name="DateRegistered")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private Date dateRegistered;

    @Column(name="IsEnrolled")
    private boolean isEnrolled;

    public MemberCourseRegistrationPK getMemberCourseRegistrationPK() {
        return memberCourseRegistrationPK;
    }

    public void setMemberCourseRegistrationPK(MemberCourseRegistrationPK memberCourseRegistrationPK) {
        this.memberCourseRegistrationPK = memberCourseRegistrationPK;
    }

    public Integer getMemberID(){return memberID;}

    public void setMemberID(Integer memberID){this.memberID = memberID;}

    public Integer getReferMemberID() {
        return referMemberID;
    }

    public void setReferMemberID(int referMemberID) {
        this.referMemberID = referMemberID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public Date getDateRegistered(){return dateRegistered;}

    public void setDateRegistered(Date dateRegistered){this.dateRegistered = dateRegistered;}

    public boolean getIsEnrolled() {
        return isEnrolled;
    }

    public void setIsEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
    }
}
