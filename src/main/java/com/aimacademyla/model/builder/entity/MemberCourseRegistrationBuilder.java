package com.aimacademyla.model.builder.entity;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.builder.GenericBuilder;

import java.time.LocalDate;

public class MemberCourseRegistrationBuilder implements GenericBuilder<MemberCourseRegistration>{

    private int memberCourseRegistrationID;
    private Member member;
    private Course course;
    private boolean isEnrolled;
    private LocalDate dateRegistered;

    @Override
    public MemberCourseRegistration build() {
        MemberCourseRegistration memberCourseRegistration = new MemberCourseRegistration();
        memberCourseRegistration.setMemberCourseRegistrationID(memberCourseRegistrationID);
        memberCourseRegistration.setMember(member);
        memberCourseRegistration.setCourse(course);
        memberCourseRegistration.setIsEnrolled(isEnrolled);
        memberCourseRegistration.setDateRegistered(dateRegistered);
        return memberCourseRegistration;
    }

    public MemberCourseRegistrationBuilder setMemberCourseRegistrationID(int memberCourseRegistrationID){
        this.memberCourseRegistrationID = memberCourseRegistrationID;
        return this;
    }

    public MemberCourseRegistrationBuilder setMember(Member member){
        this.member = member;
        return this;
    }

    public MemberCourseRegistrationBuilder setCourse(Course course){
        this.course = course;
        return this;
    }

    public MemberCourseRegistrationBuilder setIsEnrolled(boolean isEnrolled){
        this.isEnrolled = isEnrolled;
        return this;
    }

    public MemberCourseRegistrationBuilder setDateRegistered(LocalDate dateRegistered){
        this.dateRegistered = dateRegistered;
        return this;
    }
}
