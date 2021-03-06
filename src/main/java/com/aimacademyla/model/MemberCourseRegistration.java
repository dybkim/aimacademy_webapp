package com.aimacademyla.model;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by davidkim on 2/9/17.
 */

@Entity(name="Member_Course_Registration")
public class MemberCourseRegistration extends AIMEntity implements Serializable{

    private static final long serialVersionUID = 4571615710752919588L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="MemberCourseRegistrationID")
    private int memberCourseRegistrationID;

    @ManyToOne
    @JoinColumn(name="MemberID", referencedColumnName = "MemberID")
    private Member member;

    @ManyToOne
    @JoinColumn(name="CourseID", referencedColumnName = "CourseID")
    private Course course;

    @JoinColumn(name="ReferMemberID", referencedColumnName = "MemberID")
    private Member referMember;

    @Column(name="DateRegistered")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate dateRegistered;

    @Column(name="IsEnrolled")
    private boolean isEnrolled;

    @Override
    public int getBusinessID() {
        return memberCourseRegistrationID;
    }

    @Override
    public void setBusinessID(int memberCourseRegistrationID){
        this.memberCourseRegistrationID = memberCourseRegistrationID;
    }

    public int getMemberCourseRegistrationID() {
        return memberCourseRegistrationID;
    }

    public void setMemberCourseRegistrationID(int memberCourseRegistrationID) {
        this.memberCourseRegistrationID = memberCourseRegistrationID;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getDateRegistered(){return dateRegistered;}

    public void setDateRegistered(LocalDate dateRegistered){this.dateRegistered = dateRegistered;}

    public boolean getIsEnrolled() {
        return isEnrolled;
    }

    public void setIsEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
    }

    public Member getReferMember() {
        return referMember;
    }

    public void setReferMember(Member referMember) {
        this.referMember = referMember;
    }

}
